package com.example.iphoneStore.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegistration {

    @NonNull
    private final String username;
    @NonNull
    private final String password;
    @NonNull
    private final String role;
}
