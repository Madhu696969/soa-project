package com.bibliotech.fine.service;

import com.bibliotech.fine.dto.FineCalculationRequest;
import com.bibliotech.fine.entity.Fine;
import com.bibliotech.fine.entity.FineStatus;
import com.bibliotech.fine.repository.FineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineService fineService;

    @Test
    void calculateFine_shouldBeZero_whenReturnedOnOrBeforeDueDate() {
        FineCalculationRequest request = new FineCalculationRequest();
        request.setRentalId(1L);
        request.setUserId(1L);
        request.setDueDate(LocalDate.of(2026, 9, 20));
        request.setReturnDate(LocalDate.of(2026, 9, 20));

        when(fineRepository.findByRentalId(1L)).thenReturn(Optional.empty());
        when(fineRepository.save(any(Fine.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = fineService.calculateFine(request);

        assertEquals(0, response.getLateDays());
        assertEquals(BigDecimal.ZERO, response.getAmount());
        assertEquals(FineStatus.PAID, response.getStatus());
    }

    @Test
    void calculateFine_shouldChargeTenRupeesPerLateDay() {
        FineCalculationRequest request = new FineCalculationRequest();
        request.setRentalId(2L);
        request.setUserId(1L);
        request.setDueDate(LocalDate.of(2026, 9, 20));
        request.setReturnDate(LocalDate.of(2026, 9, 23));

        when(fineRepository.findByRentalId(2L)).thenReturn(Optional.empty());
        when(fineRepository.save(any(Fine.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = fineService.calculateFine(request);

        assertEquals(3, response.getLateDays());
        assertEquals(0, BigDecimal.valueOf(30).compareTo(response.getAmount()));
        assertEquals(FineStatus.PENDING, response.getStatus());
    }

    @Test
    void payFine_shouldMarkAsPaid() {
        Fine fine = new Fine();
        fine.setId(5L);
        fine.setStatus(FineStatus.PENDING);
        fine.setAmount(BigDecimal.TEN);

        when(fineRepository.findById(5L)).thenReturn(Optional.of(fine));
        when(fineRepository.save(any(Fine.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = fineService.payFine(5L);

        assertEquals(FineStatus.PAID, response.getStatus());
    }
}
