package com.example.iphoneStore.exceptions;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(Long id) {
        super("Not enough quantity of goods with id: " + id);
    }
}