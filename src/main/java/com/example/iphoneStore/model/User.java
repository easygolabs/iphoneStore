package com.example.iphoneStore.model;

import com.example.iphoneStore.emums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;
}
