package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CanPartakeInCommunity;
import com.example.Reddit.clone.ACL.OwnerCheck;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.Services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PostController {

    private PostService postService;
    private CanPartakeInCommunity canPartakeInCommunity;
    private OwnerCheck ownerCheck;


    @PreAuthorize(("@canPartakeInCommunity.userCanView(#postId)"))
    @GetMapping("/{postId}")
    public PostDTO getPost(
            @PathVariable Long postId
    ) {
        return postService.getPost(postId);
    }


    @PreAuthorize(("@canPartakeInCommunity.userCanView(#postId)"))
    @GetMapping("/not_logged_in/{postId}")
    public PostDTO getPostWithoutAuthorization(
            @PathVariable Long postId
    ) {
        return postService.getPost(postId);
    }


    @GetMapping("/communities/{page}")
    public List<PostDTO> getPostsFromCommunitiesMemberOf(
            @PathVariable Integer page
    ) {
        return postService.get20PostsFromCommunitiesMemberOf(page);
    }


    @PostMapping("/image/{postId}")
    @PreAuthorize(("@ownerCheck.userOwnsPost(#postId)"))
    public ResponseText setWallpaper(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long postId
    ) {
        return postService.setImage(file, postId);
    }


    @PreAuthorize(("@ownerCheck.userCanMakeThisPost(#postDTO, #communityId)"))
    @PostMapping("/{communityId}")
    public PostDTO saveCommunity(
            @PathVariable long communityId,
            @RequestBody PostDTO postDTO
    ) {
        return postService.savePost(postDTO, communityId);
    }


    @GetMapping("/public_communities/{page}")
    public List<PostDTO> getLatestPostsOfCommunitiesPublic(
            @PathVariable Integer page
    ) {
        return postService.getLatestPostsOfCommunitiesPublic(page);
    }


    @PreAuthorize(("@canPartakeInCommunity.userCanView(#communityId)"))
    @GetMapping("/{page}/{communityId}")
    public List<PostDTO> getLatestPostsOfCommunity(
            @PathVariable long communityId,
            @PathVariable Integer page
    ) {
        return postService.get20LatestPosts(communityId, page);
    }


    @PreAuthorize(("@canPartakeInCommunity.userCanView(#communityId)"))
    @GetMapping("/not_logged_in/{page}/{communityId}")
    public List<PostDTO> getLatestPostsOfCommunityWithoutToken(
            @PathVariable long communityId,
            @PathVariable Integer page
    ) {
        return postService.get20LatestPosts(communityId, page);
    }


    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersPosts(#username)"))
    @GetMapping("/user/{page}/{username}")
    public List<PostDTO> getLatestPostsOfUserLoggedIn(
            @PathVariable String username,
            @PathVariable Integer page
    ) {
        return postService.get20LatestPostsOfUserLoggedIn(username, page);
    }


    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersPosts(#username)"))
    @GetMapping("/user/not_logged_in/{page}/{username}")
    public List<PostDTO> getLatestPostsOfUserNotLoggedIn(
            @PathVariable String username,
            @PathVariable Integer page
    ) {
        return postService.get20LatestPostsOfUserNotLoggedIn(username, page);
    }


    @PreAuthorize(("@ownerCheck.userCanDeletePost(#postId)"))
    @DeleteMapping("/{postId}")
    public ResponseText deletePostById(@PathVariable Long postId)
    {
        return postService.deletePost(postId);
    }



}
