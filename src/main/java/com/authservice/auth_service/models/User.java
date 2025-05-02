package com.authservice.auth_service.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity(name = "auth_users")
public class User extends BaseModel{
    private String name;
    private String email;
    private String paswordHash;
    private boolean isActive;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany
    private List<Session> refreshTokens = new ArrayList<>();
}