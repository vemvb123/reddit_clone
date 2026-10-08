package com.example.Reddit.clone.ChatStuff;

import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Chat;
import com.example.Reddit.clone.Entity.ChatMessage;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Mapper.ChatMessageMapper;
import com.example.Reddit.clone.Repository.ChatMessageRepository;
import com.example.Reddit.clone.Repository.ChatRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.Services.ExceptionUtils;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/chat")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ChatController {

    private ChatServiceImpl chatService;
    private JwtService jwtService;
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private ChatMessageRepository chatMessageRepository;
    private ChatMessageMapper mapper;


    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Long chatId) {
        List<ChatMessage> messages = chatService.getChatMessages(chatId);

        List<ChatMessageDTO> messageDTOs = new ArrayList<>();
        for (ChatMessage message : messages)
            messageDTOs.add(mapper.entityToDto(message) );

        return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
    }

    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessageDTO handleChatMessage(
            @DestinationVariable Long chatId,
            @Payload ChatMessageDTO chatMessageDTO

    ) {
        Chat chat = chatRepository.findById(chatId) .orElseThrow();
        User sender = userRepository.findByUsername(chatMessageDTO.sender() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        User receiver;
        if (Objects.equals(chat.getUser1().getUsername(), sender.getUsername()))
            receiver = chat.getUser2();
        else
            receiver = chat.getUser1();


        var message = ChatMessage.builder()
                .chat(chat)
                .content(chatMessageDTO.content())
                .sentAt(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver)
                .build();

        // Return the processed message to be broadcast to subscribers
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return mapper.entityToDto(savedMessage);
    }


    @PostMapping("/getChatOrCreateIfAlreadyExists/{usernameChatWith}")
    public ResponseEntity<Long> getChatMessages(
            @PathVariable String usernameChatWith
            ) {
        User userFromToken = userRepository.findByUsername(SecurityUtils.getUsername()) .orElseThrow(() -> ExceptionUtils.noUserWithThatName( SecurityUtils.getUsername() ));
        User userChatWith = userRepository.findByUsername(usernameChatWith) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameChatWith));

        if (Objects.equals(userFromToken.getUsername(), userChatWith.getUsername())) {
            throw new RuntimeException();
        }

        List<User> usersSorted = chatService.sortUsersAlphabetically(userFromToken, userChatWith);

        //ser om chat allerede finnes, hvis det, bare returner id ac den eksiterende chatten
        Chat chatThatMightExist = chatService.getChatByUsers(usersSorted.get(0), usersSorted.get(1));
        if (chatThatMightExist != null) {
            return ResponseEntity.ok().body(chatThatMightExist.getId());
        }

        chatService.createChat(usersSorted.get(0), usersSorted.get(1));
        return ResponseEntity.ok().body(chatService.getIdOfChatBetweenUsers(usersSorted.get(0), usersSorted.get(1)));
    }

}
