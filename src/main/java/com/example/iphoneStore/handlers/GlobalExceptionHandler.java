package com.example.iphoneStore.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_MESSAGE = "errorMessage";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleInvalidJsonFormat(RuntimeException ignoredEx) {
        return new ResponseEntity<>(
                Collections.singletonMap(ERROR_MESSAGE, "Invalid JSON format"),
                HttpStatus.BAD_REQUEST
        );
    }
}
