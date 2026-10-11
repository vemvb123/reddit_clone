package com.example.authentication.DTO;

public record RegisterRequest(
    String firstname,
    String lastname,
    String email,
    String password,
    String username
) {}
