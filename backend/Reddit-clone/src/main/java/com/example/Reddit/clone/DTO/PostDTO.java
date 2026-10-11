package com.example.Reddit.clone.DTO;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostDTO(
    String title,
    String content,
    LocalDateTime createdAt,
    String pathToPostImage,
    String username,
    String communityName,
    Long id,
    String pathToCommunityImage,
    String pathToUserImage,
    LocalDateTime lastUpdated
) {}
