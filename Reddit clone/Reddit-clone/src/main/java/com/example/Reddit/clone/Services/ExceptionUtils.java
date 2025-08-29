package com.example.Reddit.clone.Services;




public class ExceptionUtils {

    public static RuntimeException noCommunityWithThatName(String communityName) {
        return new RuntimeException("No community with the name " + communityName);
    }

    public static RuntimeException noUserWithThatName(String username) {
        return new RuntimeException("No user with the name " + username);
    }

    public static void userIsNotAMemberOfCommunity(String username, String communityName) {
        throw new RuntimeException("User " + username + " is not a member of the community " + communityName + ". The user must be a member beforehand");
    }

    public static void userIsAlreadyMemberOfCommunitty(String username, String communityName) {
        throw new RuntimeException("User " + username + " is already a member of the community " + communityName + ". The user can't be a member beforehand");
    }


    public static void userIsAlreadyModeratorOfCommunity(String username, String communityName) {
        throw new RuntimeException("User " + username + " is already a moderator of the community " + communityName + ". The user can't be a moderator beforehand");
    }

    public static void userIsAlreadyAdministratorOfCommunity(String username, String communityName) {
        throw new RuntimeException("User " + username + " is already an administrator of the community " + communityName + ". The user can't be an administrator beforehand");

    }


    public static RuntimeException noCommentWithThatId(Long commentId) {
        return new RuntimeException("No comment with the id " + commentId);
    }

    public static RuntimeException noPostWithThatId(Long postId) {
        return new RuntimeException("No post with the id " + postId);

    }

    public static RuntimeException friendRequestHasAlreadyBeenSent(String toUsername, String fromUsername) {
        return new RuntimeException("There already exists a friend request from user " + fromUsername + " to " + toUsername + ". There can only be a single friend request from a specific user, to another specific user");
    }

    public static RuntimeException noExistingFriendRequestFromUserToUser(String fromUsername, String toUsername) {
        return new RuntimeException("There is no existing friend request from user " + fromUsername + " to user " + toUsername + ". There must be such a friend request before the request can be accepted");
    }
}
