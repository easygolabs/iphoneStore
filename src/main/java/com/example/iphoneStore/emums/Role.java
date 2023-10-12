package com.example.iphoneStore.emums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CLIENT,
    MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
