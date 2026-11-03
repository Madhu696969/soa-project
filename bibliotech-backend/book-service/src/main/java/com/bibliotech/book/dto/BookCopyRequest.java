package com.bibliotech.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookCopyRequest {
    @NotNull(message = "Branch id is required")
    private Long branchId;

    @NotBlank(message = "Copy code is required")
    private String copyCode;

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getCopyCode() {
        return copyCode;
    }

    public void setCopyCode(String copyCode) {
        this.copyCode = copyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCopyRequest that = (BookCopyRequest) o;
        return java.util.Objects.equals(branchId, that.branchId) &&
                java.util.Objects.equals(copyCode, that.copyCode);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(branchId, copyCode);
    }

    @Override
    public String toString() {
        return "BookCopyRequest{" +
                "branchId=" + branchId + ", " + "copyCode=" + copyCode +
                '}';
    }
}
