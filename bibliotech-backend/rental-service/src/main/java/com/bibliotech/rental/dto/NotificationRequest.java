package com.bibliotech.rental.dto;


public class NotificationRequest {
    private Long userId;
    private String message;
    private String type;

    public NotificationRequest() {
    }

    public NotificationRequest(Long userId, String message, String type) {
        this.userId = userId;
        this.message = message;
        this.type = type;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
