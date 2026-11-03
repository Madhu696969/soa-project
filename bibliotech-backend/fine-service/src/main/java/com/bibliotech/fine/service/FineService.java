package com.bibliotech.fine.service;

import com.bibliotech.fine.dto.FineCalculationRequest;
import com.bibliotech.fine.dto.FineResponse;
import com.bibliotech.fine.entity.Fine;
import com.bibliotech.fine.entity.FineStatus;
import com.bibliotech.fine.exception.ResourceNotFoundException;
import com.bibliotech.fine.repository.FineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FineService {
    private static final BigDecimal RATE_PER_DAY = BigDecimal.valueOf(10);
    private final FineRepository fineRepository;
    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }
    @Transactional
    public FineResponse calculateFine(FineCalculationRequest request) {
        long lateDays = ChronoUnit.DAYS.between(request.getDueDate(), request.getReturnDate());
        if (lateDays < 0) {
            lateDays = 0;
        }
        BigDecimal amount = RATE_PER_DAY.multiply(BigDecimal.valueOf(lateDays));
        Fine fine = fineRepository.findByRentalId(request.getRentalId()).orElseGet(Fine::new);
        fine.setRentalId(request.getRentalId());
        fine.setUserId(request.getUserId());
        fine.setLateDays((int) lateDays);
        fine.setAmount(amount);
        fine.setStatus(amount.compareTo(BigDecimal.ZERO) > 0 ? FineStatus.PENDING : FineStatus.PAID);
        return toResponse(fineRepository.save(fine));
    }

    public List<FineResponse> getMyFines(Long userId) {
        return fineRepository.findByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public FineResponse getFineByRental(Long rentalId) {
        return toResponse(fineRepository.findByRentalId(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("No fine found for this rental")));
    }

    @Transactional
    public FineResponse payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        fine.setStatus(FineStatus.PAID);
        return toResponse(fineRepository.save(fine));
    }

    private FineResponse toResponse(Fine fine) {
        return new FineResponse(fine.getId(), fine.getRentalId(), fine.getUserId(), fine.getLateDays(),
                fine.getAmount(), fine.getStatus());
    }
}
