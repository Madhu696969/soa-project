package com.bibliotech.rental.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_copy_id", nullable = false)
    private Long bookCopyId;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "borrowed_at", nullable = false)
    private LocalDate borrowedAt;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_at")
    private LocalDate returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental that = (Rental) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(bookCopyId, that.bookCopyId) &&
                java.util.Objects.equals(bookTitle, that.bookTitle) &&
                java.util.Objects.equals(borrowedAt, that.borrowedAt) &&
                java.util.Objects.equals(dueDate, that.dueDate) &&
                java.util.Objects.equals(returnedAt, that.returnedAt) &&
                java.util.Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, userId, bookCopyId, bookTitle, borrowedAt, dueDate, returnedAt, status);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id + ", " + "userId=" + userId + ", " + "bookCopyId=" + bookCopyId + ", " + "bookTitle=" + bookTitle + ", " + "borrowedAt=" + borrowedAt + ", " + "dueDate=" + dueDate + ", " + "returnedAt=" + returnedAt + ", " + "status=" + status +
                '}';
    }
}
