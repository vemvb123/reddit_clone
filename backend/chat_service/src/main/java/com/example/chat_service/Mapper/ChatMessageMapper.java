package com.example.chat_service.Mapper;

import com.example.chat_service.DTO.ChatMessageDTO;
import com.example.chat_service.Entity.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {


    public ChatMessageDTO entityToDto(ChatMessage message) {
       return new ChatMessageDTO(
               message.getContent(), message.getSender().getUsername(), message.getReceiver().getUsername(), message.getSentAt()
       );
    }

}
