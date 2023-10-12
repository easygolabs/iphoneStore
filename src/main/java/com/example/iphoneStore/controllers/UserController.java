package com.example.iphoneStore.controllers;

import com.example.iphoneStore.dto.UserRegistration;
import com.example.iphoneStore.model.User;
import com.example.iphoneStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidRole(IllegalArgumentException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, "Invalid role provided"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistration userRegistration) {
        User user = userService.registerNewUser(userRegistration);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
