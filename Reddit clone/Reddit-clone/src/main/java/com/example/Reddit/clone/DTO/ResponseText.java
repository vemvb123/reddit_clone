package com.example.Reddit.clone.DTO;

import java.time.Instant;

public record ResponseText(
    String text,
    Instant timestamp
) {
    public ResponseText(ResponseTextType textType) {
        this(textType.getMessage(), Instant.now());
    }

     public ResponseText(String text) {
        this(text, Instant.now());
    }
}
