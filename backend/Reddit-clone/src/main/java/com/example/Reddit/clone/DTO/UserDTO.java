package com.example.Reddit.clone.DTO;


public record UserDTO (
    String username,
    String pathToProfileImage,
    String password,
    String pathToWallpaperImage
) {}