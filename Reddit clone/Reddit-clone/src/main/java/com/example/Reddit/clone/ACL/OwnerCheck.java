package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Repository.*;
import com.example.Reddit.clone.Services.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
public class OwnerCheck {



    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CheckAgainstModeratorAndAdminRights checkAgainstModeratorAndAdminRights;

    @Autowired
    private MessageRepository messageRepository;

    // first determines if the post is being edited, or made for the first time
    // if its made for the first time, it determines wether the user has access to the community
    // if the post is being edited, it determines if the user is the owner of the post
    public boolean userCanMakeThisPost(String authorization, PostDTO postDTO, String communityName) {
        System.out.println("herkanskjeda");
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow();
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        System.out.println("herdaherkanskjeda");

        if (postDTO.getId() == null)
            if (community.getCommunityType() == CommunityType.PUBLIC)
                return true;
            else if (community.getCommunityType() == CommunityType.RESTRICTED || community.getCommunityType() == CommunityType.PRIVATE)
                return userRepository.findCommunitiesUserIsMemberOf(username) .contains(community);


        Post post = postRepository.findById(postDTO.getId()).orElseThrow();
        return Objects.equals(post.getUser().getId(), user.getId());
    }



    public boolean userCanViewOtherUsersPosts(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));
        System.out.println("hersajada");
        System.out.println(user.getOtherUsersCanSeePosts());
        return user.getOtherUsersCanSeePosts();
    }
    public boolean userCanViewOtherUsersComments(String username) {
        System.out.println("sadsadsda");
        User user = userRepository.findByUsername(username).orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));
        return user.getOtherUsersCanSeeComments();
    }


    public boolean userCanRequestToJoinCommunity(String authorization, String communityName) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));


        Community community = communityRepository.findByTitle(communityName) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        if (userRepository.findCommunitiesUserIsMemberOf(username).contains(community))
            return false;
        return true;
    }

    public boolean userOwnsMessage(String authorization, Long messageId) {
        User user = userRepository.findByUsername( jwtService.extractUsername(authorization) ).orElseThrow(() -> ExceptionUtils.noUserWithThatName(jwtService.extractUsername(authorization)));
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("No message with that id"));

        return (Objects.equals(message.getToUser().getId(), user.getId()));
    }

    public boolean canDeleteComment(String authorization, Long commentId) {
        System.out.println("kanskjederja");
        if (userOwnsComment(authorization, commentId))
            return true;

        System.out.println("der");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> ExceptionUtils.noCommentWithThatId(commentId));
        System.out.println("da");
        System.out.println(comment.getPost().getCommunity().getTitle());
        Community community = communityRepository.getComuntiyByTitle(comment.getPost().getCommunity().getTitle());
        System.out.println("fucki");
        boolean moderatorAllowedToDeleteComment = checkAgainstModeratorAndAdminRights.canDeleteOthersComments(authorization, comment.getPost().getCommunity().getTitle());
        System.out.println("ikikikik");
        return moderatorAllowedToDeleteComment;
    }

    public boolean userCanDeletePost(String authorization, Long postId) {
        if (userOwnsPost(authorization, postId))
            return true;

        Post post = postRepository.findById(postId) .orElseThrow(() -> ExceptionUtils.noPostWithThatId(postId));

        return checkAgainstModeratorAndAdminRights.canDeleteOthersPosts(authorization, post.getCommunity().getTitle());
    }


    public boolean userOwnsPost(String authorization, Long postId) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow();

        Post post = postRepository.findById(postId).orElseThrow();

        return Objects.equals(user.getId(), post.getUser().getId());

    }

    //skjekke: type community, bruker eier comment
    public boolean userCanMakeThisComment(String authorization, CommentDTO commentDTO) {
        System.out.println("inpreaut");
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow();

        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(() -> new RuntimeException("No post with the id " + commentDTO.getPostId()));
        Community community = communityRepository.findByTitle( post.getCommunity().getTitle() ).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(post.getCommunity().getTitle()));


        if (commentDTO.getId() == null)
            if (community.getCommunityType() == CommunityType.PUBLIC)
                return true;
            else if (community.getCommunityType() == CommunityType.RESTRICTED ||  community.getCommunityType() == CommunityType.PRIVATE)
                return userRepository.findCommunitiesUserIsMemberOf(username).contains(community);

        Comment comment = commentRepository.findById(commentDTO.getId()).orElseThrow();
        return Objects.equals(user.getId(), comment.getUser().getId());

    }


    public boolean trigger(String authorization) {

        return true;

    }



    public boolean usernameIsSameAsToken(String username, String authorization) {
        System.out.println(authorization + "fra-- ");
        String usernameFromJWT = jwtService.extractUsername(authorization);
        return Objects.equals(usernameFromJWT, username);
    }


    public boolean userOwnsComment(String authorization, Long commentId) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow();

        Comment comment = commentRepository.findById(commentId).orElseThrow();

        return Objects.equals(user.getId(), comment.getUser().getId());

    }

    public boolean userOwnsComment(String authorization, CommentDTO commentDTO) {

        if (commentDTO.getUserId() == null)
            return true;

        User user = userRepository.findByUsername( jwtService.extractUsername(authorization) ).orElseThrow();

        return Objects.equals(commentDTO.getUserId(), user.getId());

    }




    public boolean userOwnsImage(String authorization, String username) {
        String usernameOfToken = jwtService.extractUsername(authorization);
        if (Objects.equals(usernameOfToken, username))
            return true;
        return false;





    }





}
