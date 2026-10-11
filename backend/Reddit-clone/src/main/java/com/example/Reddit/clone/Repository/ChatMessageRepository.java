package com.example.Reddit.clone.Repository;

import com.example.Reddit.clone.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {



    @Query("SELECT m FROM ChatMessage m WHERE m.chat.id = :chatId ORDER BY m.sentAt ASC")
    List<ChatMessage> findByChatId(@Param("chatId") Long chatId);
}
