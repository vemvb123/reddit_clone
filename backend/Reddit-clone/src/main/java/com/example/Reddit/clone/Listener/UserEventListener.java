package com.example.Reddit.clone.Listener;

import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserRepository userRepository;

    @KafkaListener(topics = "user.created", groupId = "reddit-service")
    public void handleUserCreated(UserCreatedEvent event) {
        if (userRepository.existsByUsername(event.username()))
            return;
        User user = new User(event.username());
        userRepository.save(user);
    }
}
