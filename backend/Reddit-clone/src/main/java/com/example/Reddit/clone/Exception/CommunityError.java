package com.example.Reddit.clone.Exception;

public enum CommunityError {

    ALREADY_MEMBER,
    ALREADY_ADMINISTRATOR,
    ALREADY_MODERATOR,
    NOT_MEMBER,
    NOT_MODERATOR,
    NOT_FOUND,
    CANNOT_UNSUBSCRIBE_AS_ADMINISTRATOR,
    NOT_SENT_REQUEST;

    public String message(String username, String communityName) {
        return switch (this) {
            case ALREADY_MEMBER ->
                    "User " + username + " is already a member of the community " + communityName;

            case ALREADY_ADMINISTRATOR ->
                    "User " + username + " is already administrator of the community " + communityName;

            case ALREADY_MODERATOR ->
                    "User " + username + " is already moderator of the community " + communityName;

            case NOT_MEMBER ->
                    "User " + username + " is not a member of the community " + communityName;

            case CANNOT_UNSUBSCRIBE_AS_ADMINISTRATOR ->
                    "Cant unsubscribe user " + username + " as admin, since the user is the only admin left, while there are also still other users in the community " + communityName;

            case NOT_MODERATOR ->
                    "Can only remove moderator powers from a user that is a moderator. User " + username + " is not moderator of community " + communityName;

            case NOT_SENT_REQUEST ->
                    "User " + username + " has not sent a request to join the community " + communityName;

            default ->
                    throw new IllegalStateException(
                            "This error does not support username and communityName: " + this
                    );
        };
    }

    public String message(String communityName) {
        return switch (this) {
            case NOT_FOUND ->
                    "Community " + communityName + " was not found";

            default ->
                    throw new IllegalStateException(
                            "This error does not support only communityName: " + this
                    );
        };
    }

     public String message(long communityId) {
        return switch (this) {
            case NOT_FOUND ->
                    "Community " + communityId + " was not found";

            default ->
                    throw new IllegalStateException(
                            "This error does not support only communityName: " + this
                    );
        };
    }


}