package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.Comment;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String pathToPostImage;
    private String username;
    //private Set<Comment> comments;
    private String communityName;
    private Long id;
    private String pathToCommunityImage;
    private String pathToUserImage;
    private LocalDateTime lastUpdated;



    public PostDTO mapObjectToDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.pathToPostImage = post.getPathToPostImage();
        this.username = post.getUser().getUsername();
        //this.comments = post.getComments();
        this.communityName = post.getCommunity().getTitle();
        this.lastUpdated = post.getLastUpdated();

        this.pathToCommunityImage = post.getCommunity().getCommunityImage();
        this.pathToUserImage = post.getUser().getPathToProfileImage();


        return this;
    }

}
