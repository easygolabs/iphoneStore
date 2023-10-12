package com.example.iphoneStore.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(long orderId) {
        super("Not found. Order id: " + orderId);
    }
}
