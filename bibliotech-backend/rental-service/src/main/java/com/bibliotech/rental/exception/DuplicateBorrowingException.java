package com.bibliotech.rental.exception;

public class DuplicateBorrowingException extends RuntimeException {
    public DuplicateBorrowingException(String message) {
        super(message);
    }
}
