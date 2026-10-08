package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.CommunityType;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class CanPartakeInCommunity {

    private CommunityRepository communityRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    private JwtService jwtService;


    public boolean userCanCreate(String authorization, String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        //check if community type is public
        if (community.getCommunityType() == CommunityType.PUBLIC)
            return true;
        //check if user is part of community, if so, return true
        Set<Community> communities = userRepository.findCommunitiesByUsername(SecurityUtils.getUsername());
        for (Community communityInList : communities)
            if (communityInList == community)
                return true;
        return false;
    }


    public boolean userCanViewLoggedIn(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        if (community.getCommunityType() == CommunityType.PUBLIC
                || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        // communityType is private
        return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ) .contains(community);
    }


    public boolean userCanViewNotLoggedIn(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        if (community.getCommunityType() == CommunityType.PUBLIC
                || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        return false;
    }


    public boolean userCanView(String authorization, Long postId) {
        Post post = postRepository.findById(postId) .orElseThrow(
                () -> new NotFoundException(NotFound.POST));
        Community community = communityRepository.findByTitle(post.getCommunity().getTitle())
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        if (community.getCommunityType() == CommunityType.PUBLIC
                || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        // communityType is private
        return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ) .contains(community);
    }


    public boolean userCanView(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(NotFound.POST));
        Community community = communityRepository.findById(post.getCommunity().getId())
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        if (community.getCommunityType() == CommunityType.PUBLIC
                || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        return false;
    }


}
