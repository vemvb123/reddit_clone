package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.ChatMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {


    String content;
    String usernameSender;
    Long chatId;
    LocalDateTime sentAt;



    public ChatMessageDTO mapObjectToDTO(ChatMessage chatMessage) {
        content = chatMessage.getContent();
        usernameSender = chatMessage.getSender().getUsername();
        chatId = chatMessage.getId();
        sentAt = chatMessage.getSentAt();
        return this;
    }


}
