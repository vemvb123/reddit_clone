package com.example.authentication.Exception;

import java.time.Instant;


public record ExceptionResponse(
        int status,
        String message,
        Instant time
) {
    ExceptionResponse(int status, String message) {
        this(status, message, Instant.now());
    }
}
