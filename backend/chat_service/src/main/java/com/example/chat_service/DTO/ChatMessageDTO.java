package com.example.chat_service.DTO;


import java.time.LocalDateTime;


public record ChatMessageDTO(
    String content,
    String sender,
    String receiver,
    LocalDateTime sentAt
) {}