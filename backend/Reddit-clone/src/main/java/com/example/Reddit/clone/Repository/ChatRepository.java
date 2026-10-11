package com.example.Reddit.clone.Repository;

import com.example.Reddit.clone.Entity.Chat;
import com.example.Reddit.clone.Entity.ChatMessage;
import com.example.Reddit.clone.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.user1.username LIKE :username1 AND c.user2.username LIKE :username2")
    Chat getChatByUser1AndUser2(@Param("username1") String username1, @Param("username2") String username2);

}
