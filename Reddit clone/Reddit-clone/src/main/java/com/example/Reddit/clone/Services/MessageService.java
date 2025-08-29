package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.MessageDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.MessageRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;



    public Message saveMessageNewReplyToPost(Comment comment) {



        if (comment.getUser() == comment.getPost().getUser()) {
            System.out.println("Cant create message for having made a comment on ones own post");
            return null;
        }

        Message message = new Message();

        message.setMessageTopic(MessageTopic.NewReplyToPost);
        message.setSeen(false);
        message.setEventHappendAt(comment.getCreatedAt());
        message.setComment(comment);
        message.setToUser(comment.getPost().getUser());
        message.setFromUser(comment.getUser());

        return messageRepository.save(message);


    }
    public void saveMessageNewReplyToComment(Comment comment) {

        if (comment.getUser() == comment.getParent().getUser()) {
            System.out.println("Cant create message for having made a reply to ones own comment");
            return;
        }


        System.out.println("new comment shit!!");
        Message message = new Message();

        message.setMessageTopic(MessageTopic.NewReplyToComment);
        message.setSeen(false);
        message.setEventHappendAt(comment.getCreatedAt());
        message.setComment(comment);
        message.setToUser(comment.getParent().getUser());
        message.setFromUser(comment.getUser());

        messageRepository.save(message);
    }

    public List<MessageDTO> get10MessagesToUser(String authorization, int page) {

        User toUser = userRepository.findByUsername(jwtService.extractUsername(authorization) ).orElseThrow();

        System.out.println("user and id: " + toUser.getUsername() + ", " + toUser.getId());

        List<Message> messages = messageRepository.find10MessagesOrderByEventHappendAt(toUser.getId(), PageRequest.of(page, 10));
        System.out.println("storrelsepaquery: " + messages.size());

        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message message : messages)
            messageDTOs.add(new MessageDTO(message));



        return messageDTOs;

    }

    public List<MessageDTO> getRequestsToJoinCommunity(int page, String communityName) {
        System.out.println("getrequests....");

        List<Message> messages = messageRepository.getRequestsToJoinCommunity(communityName, PageRequest.of(page, 10));

        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message message : messages)
            messageDTOs.add(new MessageDTO(message));


        return messageDTOs;
    }


    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }



    @Autowired
    CommunityRepository communityRepository;

    public void requestToJoinCommunity(String authorization, String communityName) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));

        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));


        var message = Message.builder()
                        .messageTopic(MessageTopic.NewRequestToJoinCommunity)
                        .fromUser(user)
                        .seen(false)
                        .eventHappendAt(LocalDateTime.now())
                        .communityRequestingToJoin(community)
                        .build();



        messageRepository.save(message);



    }

    @Transactional
    public void acceptRequestToJoinCommunity(String communityName, String usernameRequestingToJoin) {
        //check if user exists
        User user = userRepository.findByUsername(usernameRequestingToJoin) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameRequestingToJoin));

        //check if community exists
        Community community = communityRepository.findByTitle(communityName) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        //check if user not already a member
        if (communityRepository.findMembersOfCommunity(communityName).contains(user))
            throw new RuntimeException("User " + user.getUsername() + " is already a member of community " + community.getTitle());

        //check if user has sent a request, if so, then delete the request, elsewise throw an error
        boolean hasSentRequestToJoinCommunity = false;
        for (Message message : messageRepository.findMessagesFromUser(user.getId()))
            if (message.getCommunityRequestingToJoin() == community) {
                hasSentRequestToJoinCommunity = true;
                messageRepository.deleteById(message.getId());
                break;
            }

        if (!hasSentRequestToJoinCommunity)
            throw new RuntimeException("User " + user.getUsername() + " has not sent a request to join the community " + community.getTitle());


        communityRepository.addUserToCommunity(user.getId(), community.getId());

    }
}
