package com.example.chat_service.Repository;

import com.example.chat_service.Entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.user1_username LIKE :username1 AND c.user2_username LIKE :username2")
    Chat getChatByUser1AndUser2(@Param("username1") String username1, @Param("username2") String username2);

}
