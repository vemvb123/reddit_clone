package com.example.Reddit.clone.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @ToString.Exclude
    private Chat chat;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String content;

    private LocalDateTime sentAt;



}
