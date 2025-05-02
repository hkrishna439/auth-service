package com.authservice.auth_service.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "roles")
@Getter
@Setter
//@JsonDeserialize(as = Role.class)
public class Role extends BaseModel {
    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}