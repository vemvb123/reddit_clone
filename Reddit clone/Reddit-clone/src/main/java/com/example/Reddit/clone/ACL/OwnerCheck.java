package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Repository.*;
import com.example.Reddit.clone.Services.ExceptionUtils;
import com.example.Reddit.clone.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    public boolean userCanMakeThisPost(PostDTO postDTO, String communityName) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        Community community = communityRepository.findByTitle(communityName).orElseThrow();

        if (postDTO.getId() == null)
            if (community.getCommunityType() == CommunityType.PUBLIC)
                return true;
            else if (community.getCommunityType() == CommunityType.RESTRICTED || community.getCommunityType() == CommunityType.PRIVATE)
                return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ) .contains(community);

        Post post = postRepository.findById(postDTO.getId()).orElseThrow();
        return Objects.equals(post.getUser().getId(), user.getId());
    }



    public boolean userCanViewOtherUsersPosts(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));
        return user.getOtherUsersCanSeePosts();
    }
    public boolean userCanViewOtherUsersComments(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));
        return user.getOtherUsersCanSeeComments();
    }


    public boolean userCanRequestToJoinCommunity(String communityName) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ) .orElseThrow(() -> ExceptionUtils.noUserWithThatName( SecurityUtils.getUsername() ));
        Community community = communityRepository.findByTitle(communityName) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));
        if (userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ).contains(community))
            return false;
        return true;
    }

    public boolean userOwnsMessage(Long messageId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow(() -> ExceptionUtils.noUserWithThatName( SecurityUtils.getUsername() ));
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("No message with that id"));

        return (Objects.equals(message.getToUser().getId(), user.getId()));
    }

    public boolean canDeleteComment(Long commentId) {
        if (userOwnsComment(commentId))
            return true;
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> ExceptionUtils.noCommentWithThatId(commentId));
        Community community = communityRepository.getComuntiyByTitle(comment.getPost().getCommunity().getTitle());
        boolean moderatorAllowedToDeleteComment = checkAgainstModeratorAndAdminRights.canDeleteOthersComments(comment.getPost().getCommunity().getTitle());
        return moderatorAllowedToDeleteComment;
    }

    public boolean userCanDeletePost(Long postId) {
        if (userOwnsPost(postId))
            return true;
        Post post = postRepository.findById(postId) .orElseThrow(() -> ExceptionUtils.noPostWithThatId(postId));
        return checkAgainstModeratorAndAdminRights.canDeleteOthersPosts(post.getCommunity().getTitle());
    }


    public boolean userOwnsPost(Long postId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        return Objects.equals(user.getId(), post.getUser().getId());
    }

    public boolean userCanMakeThisComment(CommentDTO commentDTO) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(() -> new RuntimeException("No post with the id " + commentDTO.getPostId()));
        Community community = communityRepository.findByTitle( post.getCommunity().getTitle() ).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(post.getCommunity().getTitle()));

        if (commentDTO.getId() == null)
            if (community.getCommunityType() == CommunityType.PUBLIC)
                return true;
            else if (community.getCommunityType() == CommunityType.RESTRICTED ||  community.getCommunityType() == CommunityType.PRIVATE)
                return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ).contains(community);

        Comment comment = commentRepository.findById(commentDTO.getId()).orElseThrow();
        return Objects.equals(user.getId(), comment.getUser().getId());
    }


    public boolean trigger() {
        return true;
    }


    public boolean usernameIsSameAsToken(String username, String authorization) {
        String usernameOfRequester = SecurityUtils.getUsername();
        return Objects.equals(usernameOfRequester , username);
    }


    public boolean userOwnsComment(Long commentId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return Objects.equals(user.getId(), comment.getUser().getId());
    }

    public boolean userOwnsComment(CommentDTO commentDTO) {
        if (commentDTO.getUserId() == null)
            return true;
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        return Objects.equals(commentDTO.getUserId(), user.getId());
    }


    public boolean userOwnsImage(String username) {
        String usernameOfToken = jwtService.extractUsername( SecurityUtils.getUsername() );
        return Objects.equals(usernameOfToken, username);
    }


}
