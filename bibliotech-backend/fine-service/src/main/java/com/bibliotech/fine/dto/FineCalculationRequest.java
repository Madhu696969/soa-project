package com.bibliotech.fine.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class FineCalculationRequest {
    @NotNull
    private Long rentalId;
    @NotNull
    private Long userId;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private LocalDate returnDate;

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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FineCalculationRequest that = (FineCalculationRequest) o;
        return java.util.Objects.equals(rentalId, that.rentalId) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(dueDate, that.dueDate) &&
                java.util.Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(rentalId, userId, dueDate, returnDate);
    }

    @Override
    public String toString() {
        return "FineCalculationRequest{" +
                "rentalId=" + rentalId + ", " + "userId=" + userId + ", " + "dueDate=" + dueDate + ", " + "returnDate=" + returnDate +
                '}';
    }
}
