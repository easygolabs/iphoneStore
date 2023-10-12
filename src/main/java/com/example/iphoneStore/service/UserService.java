package com.example.iphoneStore.service;

import com.example.iphoneStore.dto.UserRegistration;
import com.example.iphoneStore.emums.Role;
import com.example.iphoneStore.model.User;
import com.example.iphoneStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public User registerNewUser(UserRegistration userRegistration) {
        if (userRepository.findByUsername(userRegistration.getUsername()) != null) {
            throw new DataIntegrityViolationException("Username already exists.");
        }

        User user = new User();
        user.setUsername(userRegistration.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));

        try {
            Role userRole = Role.valueOf(userRegistration.getRole().toUpperCase());
            user.setRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role is invalid. Please provide a correct role.");
        }

        return userRepository.save(user);
    }
}
