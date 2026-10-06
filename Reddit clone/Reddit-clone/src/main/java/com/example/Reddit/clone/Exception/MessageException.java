package com.example.Reddit.clone.Exception;

public class MessageException extends RuntimeException {

    public MessageException(MessageExceptionMessage message) {
        super(message.getMessage());
    }

}
