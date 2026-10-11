package com.example.Reddit.clone.DTO;


public record MemberDTO(
    String pathToProfileImage,
    String username,
    String role,
    Boolean isFriendOfUser
) {}