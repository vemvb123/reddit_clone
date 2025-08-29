package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.User;
import lombok.Data;

@Data
public class UserDTO {



    private String username;

    private String pathToProfileImage;

    private String password;

    private String pathToWallpaperImage;


    public UserDTO mapObjectToDTO(User user) {
        username = user.getUsername();
        pathToProfileImage = user.getPathToProfileImage();
        password = user.getPassword();
        pathToWallpaperImage = user.getPathToWallpaperImage();

        return this;
    }





}
