package com.bibliotech.rental.controller;

import com.bibliotech.rental.dto.BorrowRequest;
import com.bibliotech.rental.dto.RentalResponse;
import com.bibliotech.rental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<RentalResponse> borrow(@RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.ok(rentalService.borrowBook(userId, request));
    }

    @PostMapping("/{rentalId}/return")
    public ResponseEntity<RentalResponse> returnBook(@RequestHeader("X-User-Id") Long userId,
                                                        @PathVariable Long rentalId) {
        return ResponseEntity.ok(rentalService.returnBook(rentalId, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RentalResponse>> myRentals(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(rentalService.getMyRentals(userId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<RentalResponse>> activeRentals() {
        return ResponseEntity.ok(rentalService.getActiveRentals());
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<RentalResponse>> overdueRentals() {
        return ResponseEntity.ok(rentalService.getOverdueRentals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRental(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.getRentalsByUser(userId));
    }
}
