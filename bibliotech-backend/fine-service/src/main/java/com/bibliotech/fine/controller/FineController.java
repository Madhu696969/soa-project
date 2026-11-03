package com.bibliotech.fine.controller;

import com.bibliotech.fine.dto.FineCalculationRequest;
import com.bibliotech.fine.dto.FineResponse;
import com.bibliotech.fine.service.FineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<FineResponse> calculate(@Valid @RequestBody FineCalculationRequest request) {
        return ResponseEntity.ok(fineService.calculateFine(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<FineResponse>> myFines(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(fineService.getMyFines(userId));
    }

    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<FineResponse> getByRental(@PathVariable Long rentalId) {
        return ResponseEntity.ok(fineService.getFineByRental(rentalId));
    }

    @PostMapping("/{fineId}/pay")
    public ResponseEntity<FineResponse> pay(@PathVariable Long fineId) {
        return ResponseEntity.ok(fineService.payFine(fineId));
    }
}
