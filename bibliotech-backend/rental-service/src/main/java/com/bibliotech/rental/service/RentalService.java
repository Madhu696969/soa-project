package com.bibliotech.rental.service;

import com.bibliotech.rental.client.BookClient;
import com.bibliotech.rental.client.FineClient;
import com.bibliotech.rental.client.NotificationClient;
import com.bibliotech.rental.dto.*;
import com.bibliotech.rental.entity.Rental;
import com.bibliotech.rental.entity.RentalStatus;
import com.bibliotech.rental.exception.BookUnavailableException;
import com.bibliotech.rental.exception.DuplicateBorrowingException;
import com.bibliotech.rental.exception.ResourceNotFoundException;
import com.bibliotech.rental.exception.ServiceUnavailableException;
import feign.FeignException;
import com.bibliotech.rental.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private static final int BORROW_PERIOD_DAYS = 14;

    private final RentalRepository rentalRepository;
    private final BookClient bookClient;
    private final FineClient fineClient;
    private final NotificationClient notificationClient;

    public RentalService(RentalRepository rentalRepository, BookClient bookClient,
                          FineClient fineClient, NotificationClient notificationClient) {
        this.rentalRepository = rentalRepository;
        this.bookClient = bookClient;
        this.fineClient = fineClient;
        this.notificationClient = notificationClient;
    }

    /**
     * Borrow flow: Rental Service verifies the copy is AVAILABLE via Book Service,
     * blocks duplicate borrowing of the same title by the same user, creates the
     * rental record, then instructs Book Service to flip the copy to BORROWED.
     */
    @Transactional
    public RentalResponse borrowBook(Long userId, BorrowRequest request) {
        BookCopyDto copy;
        try {
            copy = bookClient.getCopy(request.getBookCopyId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Book copy not found");
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Book Service is unreachable, please try again shortly");
        }

        if (copy == null) {
            throw new ResourceNotFoundException("Book copy not found");
        }

        // Duplicate checks run BEFORE the availability check. A copy the user already
        // holds is necessarily BORROWED, so checking availability first would report a
        // misleading 400 instead of the correct 409 CONFLICT.

        // Rule 1: cannot re-borrow the exact same physical copy while a rental is active
        boolean copyAlreadyOut = rentalRepository
                .findByBookCopyIdAndStatus(request.getBookCopyId(), RentalStatus.ACTIVE).isPresent()
                || rentalRepository
                .findByBookCopyIdAndStatus(request.getBookCopyId(), RentalStatus.OVERDUE).isPresent();

        // Rule 2: cannot hold more than one active copy of the same title
        boolean alreadyHoldsTitle =
                !rentalRepository.findByUserIdAndBookTitleAndStatus(userId, copy.getBookTitle(), RentalStatus.ACTIVE).isEmpty()
                || !rentalRepository.findByUserIdAndBookTitleAndStatus(userId, copy.getBookTitle(), RentalStatus.OVERDUE).isEmpty();

        if (alreadyHoldsTitle) {
            throw new DuplicateBorrowingException("Duplicate borrowing is not allowed. You already have this book.");
        }

        if (copyAlreadyOut) {
            throw new DuplicateBorrowingException("Duplicate borrowing is not allowed. This copy is already checked out.");
        }

        if (!"AVAILABLE".equals(copy.getStatus())) {
            throw new BookUnavailableException("This book copy is not available for borrowing");
        }

        Rental rental = new Rental();
        rental.setUserId(userId);
        rental.setBookCopyId(copy.getId());
        rental.setBookTitle(copy.getBookTitle());
        rental.setBorrowedAt(LocalDate.now());
        rental.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD_DAYS));
        rental.setStatus(RentalStatus.ACTIVE);

        Rental saved = rentalRepository.save(rental);

        bookClient.updateCopyStatus(copy.getId(), "SYSTEM", new StatusUpdateRequest("BORROWED"));

        return toResponse(saved, BigDecimal.ZERO);
    }

    /**
     * Return flow: locate rental, compute overdue days, call Fine Service to
     * calculate/store the fine, mark the rental RETURNED, flip the copy back to
     * AVAILABLE, and fire a notification.
     */
    @Transactional
    public RentalResponse returnBook(Long rentalId, Long userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

        if (!rental.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Rental not found for this user");
        }

        LocalDate returnDate = LocalDate.now();
        rental.setReturnedAt(returnDate);
        rental.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(rental);

        FineDto fine = fineClient.calculateFine(
                new FineCalculationRequest(rental.getId(), rental.getUserId(), rental.getDueDate(), returnDate));

        bookClient.updateCopyStatus(rental.getBookCopyId(), "SYSTEM", new StatusUpdateRequest("AVAILABLE"));

        String message = fine != null && fine.getAmount() != null && fine.getAmount().compareTo(BigDecimal.ZERO) > 0
                ? "You returned '" + rental.getBookTitle() + "' late. Fine charged: Rs." + fine.getAmount()
                : "You returned '" + rental.getBookTitle() + "' on time. Thank you!";

        notificationClient.createNotification(new NotificationRequest(userId, message, "RETURN"));

        return toResponse(rental, fine != null ? fine.getAmount() : BigDecimal.ZERO);
    }

    public List<RentalResponse> getMyRentals(Long userId) {
        return rentalRepository.findByUserId(userId).stream().map(r -> toResponse(r, null)).collect(Collectors.toList());
    }

    public List<RentalResponse> getActiveRentals() {
        return rentalRepository.findByStatus(RentalStatus.ACTIVE).stream().map(r -> toResponse(r, null)).collect(Collectors.toList());
    }

    public List<RentalResponse> getOverdueRentals() {
        return rentalRepository.findByStatus(RentalStatus.OVERDUE).stream().map(r -> toResponse(r, null)).collect(Collectors.toList());
    }

    public RentalResponse getRental(Long id) {
        return toResponse(rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found")), null);
    }

    public List<RentalResponse> getRentalsByUser(Long userId) {
        return rentalRepository.findByUserId(userId).stream().map(r -> toResponse(r, null)).collect(Collectors.toList());
    }

    private RentalResponse toResponse(Rental rental, BigDecimal fineAmount) {
        return new RentalResponse(rental.getId(), rental.getUserId(), rental.getBookCopyId(), rental.getBookTitle(),
                rental.getBorrowedAt(), rental.getDueDate(), rental.getReturnedAt(), rental.getStatus(), fineAmount);
    }
}
