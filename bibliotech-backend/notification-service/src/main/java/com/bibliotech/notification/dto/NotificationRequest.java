package com.bibliotech.notification.dto;

import com.bibliotech.notification.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificationRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "message is required")
    private String message;

    @NotNull(message = "type is required")
    private NotificationType type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRequest that = (NotificationRequest) o;
        return java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(message, that.message) &&
                java.util.Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(userId, message, type);
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "userId=" + userId + ", " + "message=" + message + ", " + "type=" + type +
                '}';
    }
}
