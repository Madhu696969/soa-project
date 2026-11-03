package com.bibliotech.fine.repository;

import com.bibliotech.fine.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByUserId(Long userId);
    Optional<Fine> findByRentalId(Long rentalId);
}
