package com.example.Reddit.clone.DTO;

import com.example.Reddit.clone.Entity.Comment;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.CommunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    Long id;
    String title;
    String description;
    Long userId;
    Long postId;
    Long parentCommentId;

    String communityName;

    String usernameRepliedTo;
    String usernameRepliedToPathToImage;

    String username;
    Boolean isLastChild;
    Boolean isPrimeComment;
    Boolean hasChildren;
    LocalDateTime createdAt;
    String pathToImage;
    String pathToUserImage;
    String pathToCommunityImage;


    public CommentDTO mapCommentToDTO(Comment comment, Boolean isLastChild, Boolean isPrimeComment,Boolean hasChildren) {
        if ((comment.getParent() != null) && (comment.getUser() != null) && (comment.getLastUpdated() != null)) {
            System.out.println("sannja: " + comment.getParent());
            usernameRepliedTo = comment.getParent().getUser().getUsername();
            usernameRepliedToPathToImage = comment.getParent().getUser().getPathToProfileImage();
        }

        pathToImage = comment.getPathToImage();
        communityName = comment.getPost().getCommunity().getTitle();
        pathToCommunityImage = comment.getPost().getCommunity().getCommunityImage();
        id = comment.getId();
        title = comment.getTitle();
        description = comment.getDescription();
        if (comment.getUser() != null) {
            userId = comment.getUser().getId();
            username = comment.getUser().getUsername();
            pathToUserImage = comment.getUser().getPathToProfileImage();

        }
        else
            username = "Anonymus";

        postId = comment.getPost().getId();
        createdAt = comment.getCreatedAt();

        if (comment.getParent() != null)
            parentCommentId = comment.getParent().getId();



        this.isLastChild = isLastChild;
        this.isPrimeComment = isPrimeComment;
        this.hasChildren = hasChildren;

        return this;
    }

}
