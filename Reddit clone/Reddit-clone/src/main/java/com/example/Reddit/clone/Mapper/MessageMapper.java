package com.example.Reddit.clone.Mapper;

import com.example.Reddit.clone.DTO.MessageDTO;
import com.example.Reddit.clone.Entity.Comment;
import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.MessageTopic;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {

    public MessageDTO entityToDto(Message message) {

        long messageId = message.getId();
        LocalDateTime eventHappendAt = message.getEventHappendAt();
        boolean seen = message.getSeen();

        String toUsername = null;
        String pathToToUsername = null;
        String content = null;
        Long postId = null;
        Long commentId = null;
        Long commentReceivingReplyId = null;
        Long replyingToCommentId = null;

        if (message.getToUser() != null) {
            toUsername = message.getToUser().getUsername();
            pathToToUsername = message.getToUser().getPathToProfileImage();
        }
        String fromUsername = message.getFromUser().getUsername();
        String pathToImageFromUser = message.getFromUser().getPathToProfileImage();
        MessageTopic messageTopic = message.getMessageTopic();

        if (message.getMessageTopic() == MessageTopic.NewFriendRequest)
            content = "You revieced a new friend request from user " + fromUsername;

        else if (message.getMessageTopic() == MessageTopic.NewReplyToPost) {
            content = "You recieved a new reply to your post " + message.getComment().getPost().getTitle() + " from user " + fromUsername;
            postId = message.getComment().getPost().getId();
            commentId = message.getComment().getId();
        }
        else if (message.getMessageTopic() == MessageTopic.NewReplyToComment) {
            content = "You recieved a new reply to you comment from user " + fromUsername;
            commentReceivingReplyId = message.getComment().getParent().getId();
            replyingToCommentId = message.getComment().getId();
            postId = message.getComment().getPost().getId();
        }
        else if (message.getMessageTopic() == MessageTopic.NewRequestToJoinCommunity)
            content = "User " + message.getFromUser().getUsername() + " is requesting to join this community";

        return new MessageDTO(
            eventHappendAt,
            messageTopic,
            seen,
            toUsername,
            pathToToUsername,
            content,
            fromUsername,
            pathToImageFromUser,
            messageId,
            // new reply to post
            postId,
            commentId,
            // new reply to comment
            commentReceivingReplyId,
            replyingToCommentId

        );
    }


    public Message commentToMessage(Comment comment, MessageTopic topic, boolean seen) {
        Message message = new Message();
        message.setMessageTopic(topic);
        message.setSeen(seen);
        message.setEventHappendAt(comment.getCreatedAt());
        message.setComment(comment);
        message.setToUser(comment.getPost().getUser());
        message.setFromUser(comment.getUser());
        return message;
    }

}
