package com.example.Reddit.clone.ChatStuff;

import com.example.Reddit.clone.Entity.Chat;
import com.example.Reddit.clone.Entity.ChatMessage;
import com.example.Reddit.clone.Entity.User;

import java.util.List;

public interface ChatService {
    Chat createChat(User user1, User user2);
    List<ChatMessage> getChatMessages(Long chatId);
    void sendMessage(User sender, User receiver, String content, Long chatId);

    Long getIdOfChatBetweenUsers(User user1, User user2);
}
