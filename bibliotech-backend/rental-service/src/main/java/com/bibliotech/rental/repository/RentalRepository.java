package com.bibliotech.rental.repository;

import com.bibliotech.rental.entity.Rental;
import com.bibliotech.rental.entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserId(Long userId);
    List<Rental> findByStatus(RentalStatus status);
    List<Rental> findByUserIdAndStatus(Long userId, RentalStatus status);
    Optional<Rental> findByBookCopyIdAndStatus(Long bookCopyId, RentalStatus status);
    List<Rental> findByStatusAndDueDateBefore(RentalStatus status, LocalDate date);
    List<Rental> findByUserIdAndBookTitleAndStatus(Long userId, String bookTitle, RentalStatus status);
}
