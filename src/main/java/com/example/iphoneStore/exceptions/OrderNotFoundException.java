package com.example.iphoneStore.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("Not found. Order id: " + orderId);
    }
}
