package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Services.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.Reddit.clone.ACL.CanPartakeInCommunity;
import com.example.Reddit.clone.ACL.OwnerCheck;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/post")
@EnableAutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PostController {

    final String origin = "http://localhost:3000";

    @Autowired
    private PostService postService;



    @Autowired
    private CanPartakeInCommunity canPartakeInCommunity;

    @Autowired
    private OwnerCheck ownerCheck;



    @CrossOrigin(origins = origin)
    @PreAuthorize(("@canPartakeInCommunity.userCanView(#authorization, #postId)"))
    @GetMapping("/get_post/{postId}")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String authorization
    ) {

        PostDTO postDTO = postService.getPost(postId);

        return ResponseEntity.ok().body(postDTO);
    }

    @CrossOrigin(origins = origin)
    @PreAuthorize(("@canPartakeInCommunity.userCanView(#postId)"))
    @GetMapping("/get_post_not_logged_in/{postId}")
    public ResponseEntity<PostDTO> getPostWithoutAuthorization(
            @PathVariable Long postId
    ) {

        PostDTO postDTO = postService.getPost(postId);

        return ResponseEntity.ok().body(postDTO);
    }



    @CrossOrigin(origins = origin)
    @GetMapping("/get_posts_from_communities_member_of/{page}")
    public ResponseEntity<List<PostDTO>> getPostsFromCommunitiesMemberOf(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Integer page
    ) {

        List<PostDTO> latestsPosts = postService.get20PostsFromCommunitiesMemberOf(authorization, page);

        return ResponseEntity.ok().body(latestsPosts);


    }



    //Todo: post - save file path
    @CrossOrigin(origins = origin)
    @PostMapping("/setImage/{postId}")
    @PreAuthorize(("@ownerCheck.userOwnsPost(#authorization, #postId)"))
    public ResponseEntity<String> setWallpaper(@RequestParam("file") MultipartFile file, @PathVariable Long postId, @RequestHeader("Authorization") String authorization)
    {

        postService.setImage(file, postId);

        return ResponseEntity.status(HttpStatus.OK).body("File saved successfully");
    }




    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanMakeThisPost(#authorization, #postDTO, #communityName)"))
    @PostMapping("/save_post_to_community/{communityName}")
    public ResponseEntity<PostDTO> saveCommunity(
            @PathVariable String communityName,
            @RequestBody PostDTO postDTO,
            @RequestHeader("Authorization") String authorization)
    {
        System.out.println("hallo");
        PostDTO DTOfromSavedPost = postService.savePost(postDTO, authorization, communityName);

        return ResponseEntity.ok().body(DTOfromSavedPost);
    }



    @CrossOrigin(origins = origin)
    @GetMapping("/get_all_posts_of_public_communities/{page}")
    public ResponseEntity<List<PostDTO>> getLatestPostsOfCommunitiesPublic(
            @PathVariable Integer page
    )
    {
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        List<PostDTO> latestsPosts = postService.getLatestPostsOfCommunitiesPublic(page);

        return ResponseEntity.ok().body(latestsPosts);
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@canPartakeInCommunity.userCanView(#authorization, #communityName)"))
    @GetMapping("/get_20_latets_posts_of_community/{page}/{communityName}")
    public ResponseEntity<List<PostDTO>> getLatestPostsOfCommunity(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization,
            @PathVariable Integer page
    )
    {
        List<PostDTO> latestsPosts = postService.get20LatestPosts(communityName, page);

        return ResponseEntity.ok().body(latestsPosts);
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@canPartakeInCommunity.userCanView(#communityName)"))
    @GetMapping("/get_20_latets_posts_of_community_without_token/{page}/{communityName}")
    public ResponseEntity<List<PostDTO>> getLatestPostsOfCommunityWithoutToken(
            @PathVariable String communityName,
            @PathVariable Integer page
    )
    {
        System.out.println("herfrafasa");
        List<PostDTO> latestsPosts = postService.get20LatestPosts(communityName, page);

        return ResponseEntity.ok().body(latestsPosts);
    }



    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersPosts(#username)"))
    @GetMapping("/get_20_latets_posts_of_user/{page}/{username}")
    public ResponseEntity<List<PostDTO>> getLatestPostsOfUser(
            @RequestHeader("Authorization") String authorization,
            @PathVariable String username,
            @PathVariable Integer page
    )
    {
        System.out.println("hersajada");
        List<PostDTO> latestsPosts = postService.get20LatestPostsOfUser(username, page, authorization);

        return ResponseEntity.ok().body(latestsPosts);
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersPosts(#username)"))
    @GetMapping("/get_20_latets_posts_of_user_not_logged_in/{page}/{username}")
    public ResponseEntity<List<PostDTO>> getLatestPostsOfUser(
            @PathVariable String username,
            @PathVariable Integer page
    )
    {
        System.out.println("hersajada");
        List<PostDTO> latestsPosts = postService.get20LatestPostsOfUser(username, page);

        return ResponseEntity.ok().body(latestsPosts);
    }







    @PreAuthorize(("@ownerCheck.userCanDeletePost(#authorization, #postId)"))
    @CrossOrigin(origins = origin)
    @DeleteMapping("/delete_post_by_id/{postId}")
    public ResponseEntity<String> deletePostById(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String authorization
    ) {
        System.out.println("HUH?");
        postService.deletePost(postId);

        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");

    }



}
