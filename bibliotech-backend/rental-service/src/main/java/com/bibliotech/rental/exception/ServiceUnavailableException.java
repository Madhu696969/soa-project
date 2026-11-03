package com.bibliotech.rental.exception;

/** Raised when a downstream microservice cannot be reached. */
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
