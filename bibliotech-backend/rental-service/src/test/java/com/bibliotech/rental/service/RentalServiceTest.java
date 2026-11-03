package com.bibliotech.rental.service;

import com.bibliotech.rental.client.BookClient;
import com.bibliotech.rental.client.FineClient;
import com.bibliotech.rental.client.NotificationClient;
import com.bibliotech.rental.dto.*;
import com.bibliotech.rental.entity.Rental;
import com.bibliotech.rental.entity.RentalStatus;
import com.bibliotech.rental.exception.BookUnavailableException;
import com.bibliotech.rental.exception.DuplicateBorrowingException;
import com.bibliotech.rental.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RentalServiceTest {

    private static final Long USER_ID = 5L;
    private static final Long COPY_ID = 1L;
    private static final String TITLE = "Clean Code";

    @Mock private RentalRepository rentalRepository;
    @Mock private BookClient bookClient;
    @Mock private FineClient fineClient;
    @Mock private NotificationClient notificationClient;

    @InjectMocks
    private RentalService rentalService;

    private BookCopyDto availableCopy;

    @BeforeEach
    void setUp() {
        availableCopy = new BookCopyDto();
        availableCopy.setId(COPY_ID);
        availableCopy.setBookId(10L);
        availableCopy.setBookTitle(TITLE);
        availableCopy.setStatus("AVAILABLE");

        // Default happy path: nothing is already borrowed anywhere.
        when(bookClient.getCopy(COPY_ID)).thenReturn(availableCopy);
        when(rentalRepository.findByBookCopyIdAndStatus(COPY_ID, RentalStatus.ACTIVE)).thenReturn(Optional.empty());
        when(rentalRepository.findByBookCopyIdAndStatus(COPY_ID, RentalStatus.OVERDUE)).thenReturn(Optional.empty());
        when(rentalRepository.findByUserIdAndBookTitleAndStatus(USER_ID, TITLE, RentalStatus.ACTIVE)).thenReturn(List.of());
        when(rentalRepository.findByUserIdAndBookTitleAndStatus(USER_ID, TITLE, RentalStatus.OVERDUE)).thenReturn(List.of());
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> {
            Rental r = inv.getArgument(0);
            if (r.getId() == null) {
                r.setId(100L);
            }
            return r;
        });
    }

    private BorrowRequest borrowRequest() {
        BorrowRequest request = new BorrowRequest();
        request.setBookCopyId(COPY_ID);
        return request;
    }

    @Test
    @DisplayName("Successful borrow creates an ACTIVE rental due in 14 days and marks the copy BORROWED")
    void borrowBook_shouldSucceed_whenCopyAvailableAndNoDuplicate() {
        RentalResponse response = rentalService.borrowBook(USER_ID, borrowRequest());

        assertEquals(RentalStatus.ACTIVE, response.getStatus());
        assertEquals(LocalDate.now(), response.getBorrowedAt());
        assertEquals(LocalDate.now().plusDays(14), response.getDueDate());
        assertEquals(TITLE, response.getBookTitle());
        verify(rentalRepository).save(any(Rental.class));
        verify(bookClient).updateCopyStatus(eq(COPY_ID), eq("SYSTEM"), any(StatusUpdateRequest.class));
    }

    @Test
    @DisplayName("DUPLICATE BORROWING: user already holds an ACTIVE rental of the same title -> 409")
    void borrowBook_shouldThrowConflict_whenUserAlreadyHasActiveRentalOfSameTitle() {
        Rental existing = new Rental();
        existing.setId(1L);
        when(rentalRepository.findByUserIdAndBookTitleAndStatus(USER_ID, TITLE, RentalStatus.ACTIVE))
                .thenReturn(List.of(existing));

        DuplicateBorrowingException ex = assertThrows(DuplicateBorrowingException.class,
                () -> rentalService.borrowBook(USER_ID, borrowRequest()));

        assertEquals("Duplicate borrowing is not allowed. You already have this book.", ex.getMessage());
        verify(rentalRepository, never()).save(any());
        verify(bookClient, never()).updateCopyStatus(any(), any(), any());
    }

    @Test
    @DisplayName("DUPLICATE BORROWING: an OVERDUE rental of the same title also blocks re-borrowing")
    void borrowBook_shouldThrowConflict_whenUserHasOverdueRentalOfSameTitle() {
        Rental overdue = new Rental();
        overdue.setId(2L);
        when(rentalRepository.findByUserIdAndBookTitleAndStatus(USER_ID, TITLE, RentalStatus.OVERDUE))
                .thenReturn(List.of(overdue));

        assertThrows(DuplicateBorrowingException.class,
                () -> rentalService.borrowBook(USER_ID, borrowRequest()));
        verify(rentalRepository, never()).save(any());
    }

    @Test
    @DisplayName("DUPLICATE BORROWING: the same physical copy is already checked out -> 409")
    void borrowBook_shouldThrowConflict_whenSameCopyAlreadyActivelyRented() {
        Rental existing = new Rental();
        existing.setId(3L);
        when(rentalRepository.findByBookCopyIdAndStatus(COPY_ID, RentalStatus.ACTIVE))
                .thenReturn(Optional.of(existing));

        DuplicateBorrowingException ex = assertThrows(DuplicateBorrowingException.class,
                () -> rentalService.borrowBook(USER_ID, borrowRequest()));

        assertTrue(ex.getMessage().startsWith("Duplicate borrowing is not allowed"));
        verify(rentalRepository, never()).save(any());
    }

    @Test
    @DisplayName("A copy held by somebody else is reported as unavailable, not as a duplicate")
    void borrowBook_shouldThrowBadRequest_whenCopyNotAvailable() {
        availableCopy.setStatus("BORROWED");

        assertThrows(BookUnavailableException.class,
                () -> rentalService.borrowBook(USER_ID, borrowRequest()));
        verify(rentalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Late return: fine is calculated, copy released, notification raised")
    void returnBook_shouldCalculateFineAndMarkReturned() {
        Rental rental = new Rental();
        rental.setId(50L);
        rental.setUserId(USER_ID);
        rental.setBookCopyId(COPY_ID);
        rental.setBookTitle(TITLE);
        rental.setBorrowedAt(LocalDate.now().minusDays(17));
        rental.setDueDate(LocalDate.now().minusDays(3));
        rental.setStatus(RentalStatus.OVERDUE);

        when(rentalRepository.findById(50L)).thenReturn(Optional.of(rental));

        FineDto fine = new FineDto();
        fine.setAmount(BigDecimal.valueOf(30));
        fine.setLateDays(3);
        when(fineClient.calculateFine(any(FineCalculationRequest.class))).thenReturn(fine);

        RentalResponse response = rentalService.returnBook(50L, USER_ID);

        assertEquals(RentalStatus.RETURNED, response.getStatus());
        assertEquals(LocalDate.now(), response.getReturnedAt());
        assertEquals(BigDecimal.valueOf(30), response.getFineAmount());
        verify(fineClient).calculateFine(any(FineCalculationRequest.class));
        verify(bookClient).updateCopyStatus(eq(COPY_ID), eq("SYSTEM"), any(StatusUpdateRequest.class));
        verify(notificationClient).createNotification(any(NotificationRequest.class));
    }

    @Test
    @DisplayName("On-time return produces a zero fine")
    void returnBook_onTime_shouldProduceZeroFine() {
        Rental rental = new Rental();
        rental.setId(51L);
        rental.setUserId(USER_ID);
        rental.setBookCopyId(COPY_ID);
        rental.setBookTitle(TITLE);
        rental.setDueDate(LocalDate.now().plusDays(2));
        rental.setStatus(RentalStatus.ACTIVE);

        when(rentalRepository.findById(51L)).thenReturn(Optional.of(rental));

        FineDto fine = new FineDto();
        fine.setAmount(BigDecimal.ZERO);
        fine.setLateDays(0);
        when(fineClient.calculateFine(any(FineCalculationRequest.class))).thenReturn(fine);

        RentalResponse response = rentalService.returnBook(51L, USER_ID);

        assertEquals(RentalStatus.RETURNED, response.getStatus());
        assertEquals(BigDecimal.ZERO, response.getFineAmount());
    }
}
