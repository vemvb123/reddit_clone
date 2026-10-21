package com.example.chat_service.Service;


import com.example.chat_service.DTO.ChatMessageDTO;
import com.example.chat_service.Entity.Chat;
import com.example.chat_service.Entity.ChatMessage;
import com.example.chat_service.Mapper.ChatMessageMapper;
import com.example.chat_service.Repository.ChatMessageRepository;
import com.example.chat_service.Repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    ChatRepository chatRepository;
    ChatMessageRepository chatMessageRepository;
    ChatMessageMapper mapper;


    public Chat createChat(String user1, String user2) {
        List<String> sortedUsers = sortUsersAlphabetically(user1, user2);
        return chatRepository.save(new Chat(sortedUsers.getFirst(), sortedUsers.getLast()));
    }


    public List<ChatMessageDTO> getChatMessages(Long chatId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);
        if (messages.size() == 0)
            messages = new ArrayList<ChatMessage>();

        return messages.stream()
                .map(message -> mapper.entityToDto(message))
                .toList();
    }


    public Chat getChatByUsers(String user1, String user2) {
        List<String> sortedUsers = sortUsersAlphabetically(user1, user2);
        Chat chat = chatRepository.getChatByUser1AndUser2(sortedUsers.get(0), sortedUsers.get(1));
        return chat;
    }


    public List<String> sortUsersAlphabetically(String userA, String userB) {
        List<String> usernames = new ArrayList<>();
        usernames.add(userA);
        usernames.add(userB);
        Collections.sort(usernames);
        return usernames;
    }

    public void sendMessage(String sender, String receiver, String content, Long chatId) {
        ChatMessage message = new ChatMessage();
        message.setSenderUsername(sender);
        message.setReceiverUsername(receiver);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message.setChat(chatRepository.getById(chatId));
        chatMessageRepository.save(message);
    }

    public Long getIdOfChatBetweenUsers(String user1, String user2) {
        List<String> sortedUsers = sortUsersAlphabetically(user1, user2);
        Chat chat = chatRepository.getChatByUser1AndUser2(sortedUsers.get(0), sortedUsers.get(1));
        return chat.getId();
    }


}

