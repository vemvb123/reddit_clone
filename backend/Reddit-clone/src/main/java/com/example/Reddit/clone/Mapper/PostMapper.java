package com.example.Reddit.clone.Mapper;

import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDTO entityToDto(Post post) {
        return new PostDTO(
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getPathToPostImage(),
                post.getUser().getUsername(),
                post.getCommunity().getTitle(),
                post.getId(),
                post.getPathToPostImage(),
                post.getUser().getPathToProfileImage(),
                post.getLastUpdated()
        );
    }

}