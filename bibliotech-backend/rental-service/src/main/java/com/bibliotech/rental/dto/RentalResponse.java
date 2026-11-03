package com.bibliotech.rental.dto;

import com.bibliotech.rental.entity.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalResponse {
    private Long id;
    private Long userId;
    private Long bookCopyId;
    private String bookTitle;
    private LocalDate borrowedAt;
    private LocalDate dueDate;
    private LocalDate returnedAt;
    private RentalStatus status;
    private BigDecimal fineAmount;

    public RentalResponse() {
    }

    public RentalResponse(Long id, Long userId, Long bookCopyId, String bookTitle, LocalDate borrowedAt, LocalDate dueDate, LocalDate returnedAt, RentalStatus status, BigDecimal fineAmount) {
        this.id = id;
        this.userId = userId;
        this.bookCopyId = bookCopyId;
        this.bookTitle = bookTitle;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
        this.status = status;
        this.fineAmount = fineAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalResponse that = (RentalResponse) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(bookCopyId, that.bookCopyId) &&
                java.util.Objects.equals(bookTitle, that.bookTitle) &&
                java.util.Objects.equals(borrowedAt, that.borrowedAt) &&
                java.util.Objects.equals(dueDate, that.dueDate) &&
                java.util.Objects.equals(returnedAt, that.returnedAt) &&
                java.util.Objects.equals(status, that.status) &&
                java.util.Objects.equals(fineAmount, that.fineAmount);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, userId, bookCopyId, bookTitle, borrowedAt, dueDate, returnedAt, status, fineAmount);
    }

    @Override
    public String toString() {
        return "RentalResponse{" +
                "id=" + id + ", " + "userId=" + userId + ", " + "bookCopyId=" + bookCopyId + ", " + "bookTitle=" + bookTitle + ", " + "borrowedAt=" + borrowedAt + ", " + "dueDate=" + dueDate + ", " + "returnedAt=" + returnedAt + ", " + "status=" + status + ", " + "fineAmount=" + fineAmount +
                '}';
    }
}
