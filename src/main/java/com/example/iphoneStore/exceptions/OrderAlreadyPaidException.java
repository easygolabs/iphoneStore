package com.example.iphoneStore.exceptions;

public class OrderAlreadyPaidException extends RuntimeException {
    public OrderAlreadyPaidException(Long orderId) {
        super("Order with id " + orderId + " has already been paid for.");
    }
}
