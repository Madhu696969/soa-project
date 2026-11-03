package com.bibliotech.notification.controller;

import com.bibliotech.notification.dto.NotificationRequest;
import com.bibliotech.notification.dto.NotificationResponse;
import com.bibliotech.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.create(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>> myNotifications(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(notificationService.getMyNotifications(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markRead(id));
    }
}
