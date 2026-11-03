package com.bibliotech.book.dto;

import com.bibliotech.book.entity.CopyStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull(message = "Status is required")
    private CopyStatus status;

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
        StatusUpdateRequest that = (StatusUpdateRequest) o;
        return java.util.Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(status);
    }

    @Override
    public String toString() {
        return "StatusUpdateRequest{" +
                "status=" + status +
                '}';
    }
}
