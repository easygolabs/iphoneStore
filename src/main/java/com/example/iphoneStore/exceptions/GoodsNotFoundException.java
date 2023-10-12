package com.example.iphoneStore.exceptions;

public class GoodsNotFoundException extends RuntimeException {
    public GoodsNotFoundException(Long id) {
        super("Could not find goods with id: " + id);
    }
}
