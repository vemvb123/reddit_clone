package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.MessageTopic;

import java.time.LocalDateTime;


public record MessageDTO(
     // messagedto
    LocalDateTime eventHappendAt,
    MessageTopic messageTopic,
    Boolean seen,
    String toUsername,
    String pathToToUsername,
    String content,
    String fromUsername,
    String pathToImageFromUser,
    Long messageId,
    // new reply to post
    Long postId,
    Long commentId,
    // new reply to comment
    Long commentReceivingReplyId,
    Long replyingToCommentId

) {}
