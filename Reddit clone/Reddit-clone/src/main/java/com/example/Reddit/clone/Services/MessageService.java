package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.MessageDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Exception.*;
import com.example.Reddit.clone.Mapper.MessageMapper;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.MessageRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private CommunityRepository communityRepository;

    private JwtService jwtService;

    private MessageMapper messageMapper;



    public void saveMessageNewReplyToPost(Comment comment) {
        if (comment.getUser() == comment.getPost().getUser())
            throw new MessageException(MessageExceptionMessage.OWN_POST);

        Message message = messageMapper.commentToMessage(comment, MessageTopic.NewReplyToPost, false);
        messageRepository.save(message);
    }


    public void saveMessageNewReplyToComment(Comment comment) {

        if (comment.getUser() == comment.getParent().getUser())
            throw new MessageException(MessageExceptionMessage.OWN_COMMENT);

        Message message = messageMapper.commentToMessage(comment, MessageTopic.NewReplyToComment, false);
        messageRepository.save(message);
    }


    public List<MessageDTO> get10MessagesToUser(int page) {
        User toUser = userRepository.findByUsername(SecurityUtils.getUsername()).orElseThrow();
        List<Message> messages = messageRepository.find10MessagesOrderByEventHappendAt(toUser.getId(), PageRequest.of(page, 10));

        return messages.stream()
                .map(message -> messageMapper.entityToDto(message))
                .toList();
    }


    public List<MessageDTO> getRequestsToJoinCommunity(int page, String communityName) {
        List<Message> messages = messageRepository.getRequestsToJoinCommunity(communityName, PageRequest.of(page, 10));
        return messages.stream()
                .map(message -> messageMapper.entityToDto(message))
                .toList();
    }


    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }


    public void requestToJoinCommunity(String communityName) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        var message = Message.builder()
                        .messageTopic(MessageTopic.NewRequestToJoinCommunity)
                        .fromUser(user)
                        .seen(false)
                        .eventHappendAt(LocalDateTime.now())
                        .communityRequestingToJoin(community)
                        .build();

        messageRepository.save(message);
    }


    public void acceptRequestToJoinCommunity(String communityName, String usernameRequestingToJoin) {
        //check if user exists
        User user = userRepository.findByUsername(usernameRequestingToJoin)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        //check if community exists
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        //check if user not already a member
        if (communityRepository.findMembersOfCommunity(communityName).contains(user))
            throw new CommunityException(CommunityError.ALREADY_MEMBER, user.getUsername(), communityName);
        //check if user has sent a request, if so, then delete the request, elsewise throw an error
        boolean hasSentRequestToJoinCommunity = false;
        for (Message message : messageRepository.findMessagesFromUser(user.getId()))
            if (message.getCommunityRequestingToJoin() == community) {
                hasSentRequestToJoinCommunity = true;
                messageRepository.deleteById(message.getId());
                break;
            }

        if (!hasSentRequestToJoinCommunity)
            throw new CommunityException(CommunityError.NOT_SENT_REQUEST, user.getUsername(), communityName);

        communityRepository.addUserToCommunity(user.getId(), community.getId());
    }

}
