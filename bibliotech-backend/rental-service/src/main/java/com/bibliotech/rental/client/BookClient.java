package com.bibliotech.rental.client;

import com.bibliotech.rental.dto.BookCopyDto;
import com.bibliotech.rental.dto.StatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Direct service-to-service call (via Eureka, load-balanced) to Book Service.
 * Bypasses the Gateway because this is internal east-west traffic, not a
 * client-originated request.
 */
@FeignClient(name = "BOOK-SERVICE")
public interface BookClient {

    @GetMapping("/api/books/copies/{copyId}")
    BookCopyDto getCopy(@PathVariable("copyId") Long copyId);

    @PutMapping("/api/books/copies/{copyId}/status")
    BookCopyDto updateCopyStatus(@PathVariable("copyId") Long copyId,
                                  @RequestHeader("X-User-Role") String role,
                                  @RequestBody StatusUpdateRequest request);
}
