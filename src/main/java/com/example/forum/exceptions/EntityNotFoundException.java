package com.example.forum.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, String field, String value) {
        super(String.format("%s with %s %s not found", type, field, value));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
