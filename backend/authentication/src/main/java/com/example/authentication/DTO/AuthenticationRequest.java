package com.example.authentication.DTO;

public record AuthenticationRequest(
    String username,
    String password
) {}
