package com.bibliotech.rental.dto;

import java.math.BigDecimal;

public class FineDto {
    private Long id;
    private Long rentalId;
    private Long userId;
    private int lateDays;
    private BigDecimal amount;
    private String status;

    public FineDto() {
    }

    public FineDto(Long id, Long rentalId, Long userId, int lateDays, BigDecimal amount, String status) {
        this.id = id;
        this.rentalId = rentalId;
        this.userId = userId;
        this.lateDays = lateDays;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getLateDays() {
        return lateDays;
    }

    public void setLateDays(int lateDays) {
        this.lateDays = lateDays;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FineDto that = (FineDto) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(rentalId, that.rentalId) &&
                java.util.Objects.equals(userId, that.userId) &&
                lateDays == that.lateDays &&
                java.util.Objects.equals(amount, that.amount) &&
                java.util.Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, rentalId, userId, lateDays, amount, status);
    }

    @Override
    public String toString() {
        return "FineDto{" +
                "id=" + id + ", " + "rentalId=" + rentalId + ", " + "userId=" + userId + ", " + "lateDays=" + lateDays + ", " + "amount=" + amount + ", " + "status=" + status +
                '}';
    }
}
