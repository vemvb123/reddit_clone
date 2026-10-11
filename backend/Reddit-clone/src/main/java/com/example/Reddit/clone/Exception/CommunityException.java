package com.example.Reddit.clone.Exception;

public class CommunityException extends RuntimeException {

    public CommunityException(CommunityError error, String username, String communityName) {
        super(error.message(username, communityName));
    }

    public CommunityException(CommunityError error, String communityName) {
        super(error.message(communityName));
    }

     public CommunityException(CommunityError error, long communityId) {
        super(error.message(communityId));
    }

}
