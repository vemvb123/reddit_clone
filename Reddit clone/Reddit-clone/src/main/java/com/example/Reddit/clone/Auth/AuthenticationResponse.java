package com.example.Reddit.clone.Auth;

import com.example.Reddit.clone.Entity.RoleEnum;

import java.util.HashSet;
import java.util.Set;

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