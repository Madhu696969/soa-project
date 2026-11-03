package com.bibliotech.rental.client;

import com.bibliotech.rental.dto.FineCalculationRequest;
import com.bibliotech.rental.dto.FineDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "FINE-SERVICE")
public interface FineClient {

    @PostMapping("/api/fines/calculate")
    FineDto calculateFine(@RequestBody FineCalculationRequest request);
}
