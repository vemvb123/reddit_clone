package com.example.Reddit.clone.Mapper;

import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.Entity.Comment;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class CommentMapper {


    public Comment dtoToEntity(CommentDTO dto, Post post, Comment parentComment, User user) {
        Comment comment = new Comment();
        if (dto.id() != null) {
            comment.setId(dto.id());
            comment.setLastUpdated(LocalDateTime.now());
            comment.setCreatedAt(dto.createdAt());
        }
        else
            comment.setCreatedAt(LocalDateTime.now());

        comment.setPathToImage(dto.pathToImage());
        comment.setDescription(dto.description());
        comment.setTitle(dto.title());
        comment.setPost(post);
        comment.setParent(parentComment);
        comment.setChildren(new HashSet<>());
        comment.setIsPrimeComment(dto.isPrimeComment());
        comment.setUser(user);

        return comment;
    }

    public CommentDTO entityToDTO(Comment comment, Boolean isLastChild, Boolean isPrimeComment, Boolean hasChildren) {
        if ((comment.getParent() != null) && (comment.getUser() != null) && (comment.getLastUpdated() != null)) {
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
