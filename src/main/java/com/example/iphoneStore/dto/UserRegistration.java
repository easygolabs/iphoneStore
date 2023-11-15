package com.example.iphoneStore.dto;

import lombok.Data;

@Data
public class UserRegistration {

    private final String username;
    private final String password;
    private final String role;
}
