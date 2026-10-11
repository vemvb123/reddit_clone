package com.example.authentication.Exception;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String username) {
        super("User with username " + username + " not found");
    }


}
