package com.example.Reddit.clone.ChatStuff;


import java.time.LocalDateTime;


public record ChatMessageDTO(
    String content,
    String sender,
    String receiver,
    LocalDateTime sentAt
) {}