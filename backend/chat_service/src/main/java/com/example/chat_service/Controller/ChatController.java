package com.example.chat_service.Controller;



import com.example.chat_service.DTO.ChatMessageDTO;
import com.example.chat_service.Entity.Chat;
import com.example.chat_service.Entity.ChatMessage;
import com.example.chat_service.Mapper.ChatMessageMapper;
import com.example.chat_service.Repository.ChatMessageRepository;
import com.example.chat_service.Repository.ChatRepository;
import com.example.chat_service.Service.ChatService;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.utils.SecurityUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/chat")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ChatController {

    private ChatService chatService;
    private ChatRepository chatRepository;
    private ChatMessageRepository chatMessageRepository;
    private ChatMessageMapper mapper;


    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Long chatId) {
        List<ChatMessageDTO> messages = chatService.getChatMessages(chatId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessageDTO handleChatMessage(
            @DestinationVariable Long chatId,
            @Payload ChatMessageDTO chatMessageDTO

    ) {
        Chat chat = chatRepository.findById(chatId) .orElseThrow();
        String sender = chatMessageDTO.sender();

        String receiver;
        if (Objects.equals(chat.getUser1_username(), sender))
            receiver = chat.getUser2_username();
        else
            receiver = chat.getUser1_username();

        ChatMessage message = new ChatMessage();
        message.setSenderUsername(sender);
        message.setReceiverUsername(receiver);
        message.setContent(chatMessageDTO.content());
        message.setSentAt(LocalDateTime.now());
        message.setChat(chat);

        // Return the processed message to be broadcast to subscribers
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return mapper.entityToDto(savedMessage);
    }


    @PostMapping("/getChatOrCreateIfAlreadyExists/{usernameChatWith}")
    public ResponseEntity<Long> getChatMessages(
            @PathVariable String usernameChatWith
            ) {
        String userFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> usersSorted = chatService.sortUsersAlphabetically(userFromToken, usernameChatWith);

        //ser om chat allerede finnes, hvis det, bare returner id ac den eksiterende chatten
        Chat chatThatMightExist = chatService.getChatByUsers(usersSorted.get(0), usersSorted.get(1));
        if (chatThatMightExist != null) {
            return ResponseEntity.ok().body(chatThatMightExist.getId());
        }

        chatService.createChat(usersSorted.get(0), usersSorted.get(1));
        return ResponseEntity.ok().body(chatService.getIdOfChatBetweenUsers(usersSorted.get(0), usersSorted.get(1)));
    }

}
