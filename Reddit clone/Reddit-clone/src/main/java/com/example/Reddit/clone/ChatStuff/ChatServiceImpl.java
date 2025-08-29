package com.example.Reddit.clone.ChatStuff;

import com.example.Reddit.clone.Entity.Chat;
import com.example.Reddit.clone.Entity.ChatMessage;
import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.ChatMessageRepository;
import com.example.Reddit.clone.Repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ChatServiceImpl implements ChatService {


    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Override
    public Chat createChat(User user1, User user2) {
        List<User> sortedUsers = sortUsersAlphabetically(user1, user2);

        var chat = Chat.builder()
                .user1(sortedUsers.get(0))
                .user2(sortedUsers.get(1))
                .build();


        return chatRepository.save(chat);
    }

    @Override
    public List<ChatMessage> getChatMessages(Long chatId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);
        System.out.println("huuuuuuu " + messages);
        if (messages.size() == 0) {
            System.out.println("size is 0");
            return new ArrayList<ChatMessage>();

        }
        else return messages;
    }

    public Chat getChatByUsers(User user1, User user2) {
        List<User> sortedUsers = sortUsersAlphabetically(user1, user2);
        Chat chat = chatRepository.getChatByUser1AndUser2(sortedUsers.get(0).getUsername(), sortedUsers.get(1).getUsername());
        return chat;
    }



    public List<User> sortUsersAlphabetically(User userA, User userB) {
        List<String> usernames = new ArrayList<>();
        usernames.add(userA.getUsername());
        usernames.add(userB.getUsername());
        Collections.sort(usernames);

        List<User> users = new ArrayList<>();
        for (String username : usernames) {
            if (Objects.equals(username, userA.getUsername()))
                users.add(userA);
            else
                users.add(userB);
        }

        return users;
    }

    @Override
    public void sendMessage(User sender, User receiver, String content, Long chatId) {

        var message = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sentAt(LocalDateTime.now())
                .chat(chatRepository.getById(chatId))
                .build();

        chatMessageRepository.save(message);

    }

    @Override
    public Long getIdOfChatBetweenUsers(User user1, User user2) {

        List<User> sortedUsers = sortUsersAlphabetically(user1, user2);
        Chat chat = chatRepository.getChatByUser1AndUser2(sortedUsers.get(0).getUsername(), sortedUsers.get(1).getUsername());
        return chat.getId();
    }
    // Implementation of methods
}

