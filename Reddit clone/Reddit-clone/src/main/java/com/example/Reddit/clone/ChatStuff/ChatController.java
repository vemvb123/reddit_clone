package com.example.Reddit.clone.ChatStuff;

import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Chat;
import com.example.Reddit.clone.Entity.ChatMessage;
import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.ChatMessageRepository;
import com.example.Reddit.clone.Repository.ChatRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.Services.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ChatController {
    final String origin = "http://localhost:3000";

    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @CrossOrigin(origins = origin)
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Long chatId) {
        System.out.println("kanskje");
        List<ChatMessage> messages = chatService.getChatMessages(chatId);

        List<ChatMessageDTO> messageDTOs = new ArrayList<>();
        for (ChatMessage message : messages)
            messageDTOs.add( new ChatMessageDTO().mapObjectToDTO(message) );

        return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
    }

    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessageDTO handleChatMessage(
            @DestinationVariable Long chatId,
            @Payload ChatMessageDTO chatMessageDTO

    ) {
        System.out.println("handling message");

        Chat chat = chatRepository.findById(chatId) .orElseThrow();
        User sender = userRepository.findByUsername(chatMessageDTO.getSender() ).orElseThrow();

        User receiver;
        if (Objects.equals(chat.getUser1().getUsername(), sender.getUsername()))
            receiver = chat.getUser2();
        else
            receiver = chat.getUser1();


        var message = ChatMessage.builder()
                .chat(chat)
                .content(chatMessageDTO.getContent())
                .sentAt(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver)
                .build();


        // Return the processed message to be broadcast to subscribers
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return new ChatMessageDTO().mapObjectToDTO(savedMessage);
    }


    @CrossOrigin(origins = origin)
    @PostMapping("/getChatOrCreateIfAlreadyExists/{usernameChatWith}")
    public ResponseEntity<Long> getChatMessages(
            @RequestHeader("Authorization") String authorization,
            @PathVariable String usernameChatWith
            ) {
        System.out.println("HJJSAHJSAJHD");
        String usernameFromToken = jwtService.extractUsername(authorization);
        User userFromToken = userRepository.findByUsername(usernameFromToken) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameFromToken));

        User userChatWith = userRepository.findByUsername(usernameChatWith) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameChatWith));

        if (Objects.equals(userFromToken.getUsername(), userChatWith.getUsername())) {
            System.out.println(userFromToken.getUsername());
            System.out.println(userChatWith.getUsername());
            throw new RuntimeException("no lol");
        }


        List<User> usersSorted = chatService.sortUsersAlphabetically(userFromToken, userChatWith);

        //ser om chat allerede finnes, hvis det, bare returner id ac den eksiterende chatten
        Chat chatThatMightExist = chatService.getChatByUsers(usersSorted.get(0), usersSorted.get(1));
        if (chatThatMightExist != null) {
            System.out.println("chatexists");
            System.out.println(chatThatMightExist.getId());
            return ResponseEntity.ok().body(chatThatMightExist.getId());
        }



        chatService.createChat(usersSorted.get(0), usersSorted.get(1));
        System.out.println("HERERDET");
        System.out.println(chatService.getIdOfChatBetweenUsers(usersSorted.get(0), usersSorted.get(1)));
        return ResponseEntity.ok().body(chatService.getIdOfChatBetweenUsers(usersSorted.get(0), usersSorted.get(1)));



    }
}
