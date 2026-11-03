package com.bibliotech.rental.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequest {
    @NotNull(message = "bookCopyId is required")
    private Long bookCopyId;

    public Long getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRequest that = (BorrowRequest) o;
        return java.util.Objects.equals(bookCopyId, that.bookCopyId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(bookCopyId);
    }

    @Override
    public String toString() {
        return "BorrowRequest{" +
                "bookCopyId=" + bookCopyId +
                '}';
    }
}
