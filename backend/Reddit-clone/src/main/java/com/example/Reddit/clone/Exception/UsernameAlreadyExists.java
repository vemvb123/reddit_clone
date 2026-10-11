package com.example.Reddit.clone.Exception;

public class UsernameAlreadyExists extends Exception {

    public UsernameAlreadyExists() {
        super("That username is already taken");
    }

}
