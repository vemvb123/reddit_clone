package com.example.Reddit.clone.Auth;

public record RegisterRequest(
    String firstname,
    String lastname,
    String email,
    String password,
    String username
) {}