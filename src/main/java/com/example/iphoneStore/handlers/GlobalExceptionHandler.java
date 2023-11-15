package com.example.iphoneStore.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String ERROR_MESSAGE = "errorMessage";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleInvalidJsonFormat(RuntimeException ex) {
        log.error("Unhandled exception.", ex);
        return new ResponseEntity<>(
                Collections.singletonMap(ERROR_MESSAGE, "Invalid JSON format"),
                HttpStatus.BAD_REQUEST
        );
    }
}
