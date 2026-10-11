package com.example.authentication.Exception;

public class UsernameAlreadyExists extends RuntimeException {

    public UsernameAlreadyExists(String username) {
        super("The username " + username + " is already taken");
    }

}
