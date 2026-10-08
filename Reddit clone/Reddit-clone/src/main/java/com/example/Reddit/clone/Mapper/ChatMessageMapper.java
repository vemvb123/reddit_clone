package com.example.Reddit.clone.Mapper;

import com.example.Reddit.clone.ChatStuff.ChatMessageDTO;
import com.example.Reddit.clone.Entity.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {


    public ChatMessageDTO entityToDto(ChatMessage message) {
       return new ChatMessageDTO(
               message.getContent(), message.getSender().getUsername(), message.getReceiver().getUsername(), message.getSentAt()
       );
    }

}
