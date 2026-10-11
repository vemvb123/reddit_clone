package com.example.Reddit.clone.Exception;

public class UserException extends RuntimeException {

    public UserException() {
        super("User not found");
    }

}
