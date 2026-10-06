package com.example.Reddit.clone.Exception;

public enum MessageExceptionMessage {

    OWN_POST("Cannot make message for commenting on one's own post"),
    OWN_COMMENT("Cannot make message for commenting on one's own comment");

    private final String message;

    MessageExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}