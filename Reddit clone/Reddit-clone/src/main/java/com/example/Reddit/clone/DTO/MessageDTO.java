package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.MessageTopic;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;


@ToString(callSuper = true)
@Data
public class MessageDTO {

    // messagedto
    LocalDateTime eventHappendAt;
    MessageTopic messageTopic;
    Boolean seen;
    String toUsername;
    String pathToToUsername;
    String content;
    String fromUsername;
    private String pathToImageFromUser;
    private Long messageId;


    // new reply to post
    private Long postId;
    private Long commentId;

    // new reply to comment
    private Long commentReceivingReplyId;
    private Long replyingToCommentId;



    public MessageDTO(Message message) {
        messageId = message.getId();
        eventHappendAt = message.getEventHappendAt();
        seen = message.getSeen();
        if (message.getToUser() != null) {
            toUsername = message.getToUser().getUsername();
            pathToToUsername = message.getToUser().getPathToProfileImage();
        }
        fromUsername = message.getFromUser().getUsername();
        pathToImageFromUser = message.getFromUser().getPathToProfileImage();
        messageTopic = message.getMessageTopic();

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
        else if (message.getMessageTopic() == MessageTopic.NewRequestToJoinCommunity) {
            content = "User " + message.getFromUser().getUsername() + " is requesting to join this community";
        }


    }


}
