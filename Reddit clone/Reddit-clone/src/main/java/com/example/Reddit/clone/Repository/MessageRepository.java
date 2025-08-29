package com.example.Reddit.clone.Repository;


import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query(
            "SELECT m FROM Message m WHERE " +
            "m.toUser.id = :toUserId " +
            "ORDER BY m.eventHappendAt DESC"
    )
    List<Message> find10MessagesOrderByEventHappendAt(@Param("toUserId") Long toUserId, Pageable pageable);

    @Query(
            "SELECT m FROM Message m WHERE " +
                    "m.toUser.id = :toUserId " +
                    "ORDER BY m.eventHappendAt DESC"
    )
    List<Message> findMessagesOrderByEventHappendAt(@Param("toUserId") Long toUserId);


    @Query("SELECT m FROM Message m WHERE m.fromUser.id = :fromUserId ORDER BY m.eventHappendAt DESC")
    List <Message> findMessagesFromUser(@Param("fromUserId") Long fromUserId);



    @Query(
            "SELECT m FROM Message m WHERE " +
            "m.toUser.username LIKE :toUsername " +
            "AND m.fromUser.username LIKE :fromUsername"
    )
    Message findFriendRequestFromUserToUser(@Param("fromUsername") String fromUsername, @Param("toUsername") String toUsername);

    @Query("SELECT m FROM Message m WHERE m.communityRequestingToJoin IS NOT NULL AND m.communityRequestingToJoin.title = :communityName")
    List<Message> getRequestsToJoinCommunity(@Param("communityName") String communityName, PageRequest of);


    //@Query("SELECT c.requestsToJoinCommunity FROM Community c WHERE title LIKE :communityName ORDER BY m.eventHappendAt DESC ")
    //List<Message> getRequestsToJoinCommunity(@Param("communityName") String communityName, PageRequest of);










}
