package com.staffdesk.exception;

/**
 * Custom runtime exception thrown when a requested resource (like an Employee by ID) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
