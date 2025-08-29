package com.example.Reddit.clone.ChatStuff;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    final String origin = "http://localhost:3000";

    @Override
    @CrossOrigin(origins = origin)
    public void configureMessageBroker(MessageBrokerRegistry config) {
        System.out.println("neeeeeeeeeeeeeeeeeeeeeeeeeeeei");
        config.enableSimpleBroker("/topic");  // Enables a simple in-memory message broker
        config.setApplicationDestinationPrefixes("/app");  // Defines the prefix for messages that are sent to the message broker
    }

    @Override
    @CrossOrigin(origins = origin)
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("jaaaaaaaaaaaaaaaaaaaaaa");
        registry.addEndpoint("/websocket")  // Defines the endpoint for WebSocket communication
                .setAllowedOrigins("http://localhost:3000")  // Replace with your actual frontend URL
                .withSockJS();  // Enables SockJS fallback options for browsers that do not support WebSocket
    }














}

