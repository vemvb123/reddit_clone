package com.example.Reddit.clone.DTO;

public enum ResponseTextType {

    DELETED("Deleted successfully"),
    FILE_SAVED("File saved successfully"),
    CHANGED_RIGHTS("Moderator rights changed successfully"),
    REMOVE_MOD("Removed moderator"),
    BECAME_MEMBER("User became moderator successfully");

    private final String message;

    ResponseTextType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
}
