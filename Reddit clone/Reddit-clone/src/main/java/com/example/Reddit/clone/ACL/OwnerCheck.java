package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Repository.*;
import com.example.Reddit.clone.Services.ExceptionUtils;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class OwnerCheck {

    private CommunityRepository communityRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private JwtService jwtService;
    private CheckAgainstModeratorAndAdminRights checkAgainstModeratorAndAdminRights;
    private MessageRepository messageRepository;

    // first determines if the post is being edited, or made for the first time
    // if its made for the first time, it determines wether the user has access to the community
    // if the post is being edited, it determines if the user is the owner of the post
    public boolean userCanMakeThisPost(PostDTO postDTO, long communityId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        if (postDTO.id() == null)
            if (community.getCommunityType() == CommunityType.PUBLIC)
                return true;
            else if (community.getCommunityType() == CommunityType.RESTRICTED
                    || community.getCommunityType() == CommunityType.PRIVATE)
                return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ) .contains(community);

        Post post = postRepository.findById(postDTO.id())
                .orElseThrow(() -> new NotFoundException(NotFound.POST));
        return Objects.equals(post.getUser().getId(), user.getId());
    }


    public boolean userCanViewOtherUsersPosts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        return user.getOtherUsersCanSeePosts();
    }


    public boolean userCanViewOtherUsersComments(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        return user.getOtherUsersCanSeeComments();
    }


    public boolean userCanRequestToJoinCommunity(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        if (userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ).contains(community))
            return false;
        return true;
    }


    public boolean userOwnsMessage(long messageId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException(NotFound.MESSAGE));

        long userInMessage = message.getToUser().getId();
        return (Objects.equals(userInMessage, user.getId()));
    }

    public boolean canDeleteComment(long commentId) {
        if (userOwnsComment(commentId))
            return true;
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));
        long communityId = comment.getPost().getId();
        return checkAgainstModeratorAndAdminRights.canDeleteOthersComments(communityId);
    }

    public boolean userCanDeletePost(long postId) {
        if (userOwnsPost(postId))
            return true;
        Post post = postRepository.findById(postId) .orElseThrow(() -> ExceptionUtils.noPostWithThatId(postId));
        return checkAgainstModeratorAndAdminRights.canDeleteOthersPosts(post.getCommunity().getId());
    }


    public boolean userOwnsPost(long postId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        return Objects.equals(user.getId(), post.getUser().getId());
    }

    public boolean userCanMakeThisComment(CommentDTO commentDTO) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Post post = postRepository.findById(commentDTO.postId())
                .orElseThrow(() -> new NotFoundException(NotFound.POST));
        Community community = communityRepository.findById( post.getCommunity().getId() )
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        if (commentDTO.id() == null)
            // Community is public
            if (community.getCommunityType() == CommunityType.PUBLIC)
                return true;
            // community is restricted or private, then user has to be a member
            else if (community.getCommunityType() == CommunityType.RESTRICTED
                    ||  community.getCommunityType() == CommunityType.PRIVATE)
                return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ).contains(community);
        // user owns the comment
        Comment comment = commentRepository.findById(commentDTO.id())
                .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));
        long userIdOfComment = comment.getUser().getId();
        return Objects.equals(user.getId(), userIdOfComment);
    }


    public boolean trigger() {
        return true;
    }


    public boolean usernameIsSameAsToken(String username, String authorization) {
        String usernameOfRequester = SecurityUtils.getUsername();
        return Objects.equals(usernameOfRequester , username);
    }


    public boolean usernameIsSameAsToken(String username) {
        String usernameOfRequester = SecurityUtils.getUsername();
        return Objects.equals(usernameOfRequester , username);
    }


    public boolean userOwnsComment(Long commentId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));

        long userIdOfComment = comment.getUser().getId();
        return Objects.equals(user.getId(), userIdOfComment);
    }


    public boolean userOwnsComment(CommentDTO commentDTO) {
        if (commentDTO.id() == null)
            return true;
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        return Objects.equals(commentDTO.userId(), user.getId());
    }


    public boolean userOwnsImage(String username) {
        String usernameOfToken = jwtService.extractUsername( SecurityUtils.getUsername() );
        return Objects.equals(usernameOfToken, username);
    }


}
