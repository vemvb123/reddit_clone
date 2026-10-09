package com.example.Reddit.clone.DTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDTO(

    Long id,
    String title,
    String description,
    Long userId,
    Long postId,
    Long parentCommentId,

    String communityName,

    String usernameRepliedTo,
    String usernameRepliedToPathToImage,

    String username,
    Boolean isLastChild,
    Boolean isPrimeComment,
    Boolean hasChildren,
    LocalDateTime createdAt,
    String pathToImage,
    String pathToUserImage,
    String pathToCommunityImage

) {}

