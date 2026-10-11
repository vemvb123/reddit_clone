package com.example.authentication.DTO;

import com.example.authentication.Entity.RoleEnum;

import java.util.HashSet;

public record AuthenticationResponse(
    String firstname,
    String lastname,
    String email,
    String username,
    Set<RoleEnum> roles,
    String token
) {
    public AuthenticationResponse(
        String firstname,
        String lastname,
        String email,
        String username,
        String token
    ) {
        this(firstname, lastname, email, username, new HashSet<>(), token);
    }

    public AuthenticationResponse(
        String token
    ) {
        this(null, null, null, null, null, token);
    }

}