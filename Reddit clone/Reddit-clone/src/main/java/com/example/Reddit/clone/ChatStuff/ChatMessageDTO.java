package com.example.Reddit.clone.ChatStuff;


import com.example.Reddit.clone.Entity.ChatMessage;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {

    String content;
    String sender;
    String reciever;
    LocalDateTime sentAt;

    public ChatMessageDTO mapObjectToDTO(ChatMessage chatMessage) {
        content = chatMessage.getContent();
        sender = chatMessage.getSender().getUsername();
        reciever = chatMessage.getReceiver().getUsername();
        sentAt = chatMessage.getSentAt();
        return this;
    }


}
