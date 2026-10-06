package com.example.Reddit.clone.Mapper;

import com.example.Reddit.clone.DTO.UserDTO;
import com.example.Reddit.clone.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO entityToDto(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPathToProfileImage(),
                user.getPassword(),
                user.getPathToWallpaperImage()
        );
    }

}
