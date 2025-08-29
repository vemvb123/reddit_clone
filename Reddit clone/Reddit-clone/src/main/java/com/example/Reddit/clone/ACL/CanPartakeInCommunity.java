package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.CommunityType;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.Services.ExceptionUtils;
import com.example.Reddit.clone.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CanPartakeInCommunity {


    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
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
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        if (community.getCommunityType() == CommunityType.PUBLIC || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        // communityType is private
        return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ) .contains(community);
    }

    public boolean userCanViewNotLoggedIn(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();

        if (community.getCommunityType() == CommunityType.PUBLIC || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        return false;
    }

    public boolean userCanView(String authorization, Long postId) {
        Post post = postRepository.findById(postId) .orElseThrow(() -> ExceptionUtils.noPostWithThatId(postId));

        Community community = communityRepository.findByTitle(post.getCommunity().getTitle()).orElseThrow();

        if (community.getCommunityType() == CommunityType.PUBLIC || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;

        // communityType is private
        return userRepository.findCommunitiesUserIsMemberOf( SecurityUtils.getUsername() ) .contains(community);
    }


    public boolean userCanView(Long postId) {
        Post post = postRepository.findById(postId) .orElseThrow(() -> ExceptionUtils.noPostWithThatId(postId));

        Community community = communityRepository.findByTitle(post.getCommunity().getTitle()).orElseThrow();

        if (community.getCommunityType() == CommunityType.PUBLIC || community.getCommunityType() == CommunityType.RESTRICTED)
            return true;
        return false;
    }


}
