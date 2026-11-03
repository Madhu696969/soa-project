package com.bibliotech.book.dto;

import com.bibliotech.book.entity.CopyStatus;

public class BookCopyResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long branchId;
    private String branchName;
    private String copyCode;
    private CopyStatus status;

    public BookCopyResponse() {
    }

    public BookCopyResponse(Long id, Long bookId, String bookTitle, Long branchId, String branchName, String copyCode, CopyStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.branchId = branchId;
        this.branchName = branchName;
        this.copyCode = copyCode;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCopyCode() {
        return copyCode;
    }

    public void setCopyCode(String copyCode) {
        this.copyCode = copyCode;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCopyResponse that = (BookCopyResponse) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(bookId, that.bookId) &&
                java.util.Objects.equals(bookTitle, that.bookTitle) &&
                java.util.Objects.equals(branchId, that.branchId) &&
                java.util.Objects.equals(branchName, that.branchName) &&
                java.util.Objects.equals(copyCode, that.copyCode) &&
                java.util.Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, bookId, bookTitle, branchId, branchName, copyCode, status);
    }

    @Override
    public String toString() {
        return "BookCopyResponse{" +
                "id=" + id + ", " + "bookId=" + bookId + ", " + "bookTitle=" + bookTitle + ", " + "branchId=" + branchId + ", " + "branchName=" + branchName + ", " + "copyCode=" + copyCode + ", " + "status=" + status +
                '}';
    }
}
