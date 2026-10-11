package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.OwnerCheck;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.DTO.ResponseTextType;
import com.example.Reddit.clone.Services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/comment")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CommentController {

    private CommentService commentService;
    private OwnerCheck ownerCheck;

    @GetMapping("/{commentId}")
    public CommentDTO getComment(
            @PathVariable Long commentId
    ) {
        return commentService.getComment(commentId);
    }


    @PreAuthorize(("@ownerCheck.canDeleteComment(#commentId)"))
    @DeleteMapping("/{commentId}")
    public ResponseText deleteComment(
            @PathVariable Long commentId
    ) {
        return commentService.deleteComment(commentId);
    }

    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersComments(#username)"))
    @GetMapping("/latests/{page}/{username}")
    public List<CommentDTO> getLatestCommentsOfUserLoggedIn(
            @PathVariable String username,
            @PathVariable Integer page
    ) {
        return commentService.get20LatestCommentsOfUserLoggedIn(username, page);
    }

    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersComments(#username)"))
    @GetMapping("/latets_not_logged_in/{page}/{username}")
    public List<CommentDTO> getLatestCommentsOfUserNotLoggedIn(
            @PathVariable String username,
            @PathVariable Integer page
    ) {
        return commentService.get20LatestCommentsOfUserNotLoggedIn(username, page);
    }


    @PreAuthorize(("@ownerCheck.userCanMakeThisComment(#commentDTO)"))
    @PostMapping
    public CommentDTO saveComment(@RequestBody CommentDTO commentDTO)
    {
        return commentService.saveComment(commentDTO);
    }


    @PreAuthorize(("@ownerCheck.userOwnsComment(#commentId)"))
    @PostMapping("/image/{commentId}")
    public ResponseText setWallpaper(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long commentId
    ) {
        commentService.setImage(file, commentId);
        return new ResponseText(ResponseTextType.FILE_SAVED);
    }



    //will return 10 comments from a certain point
    //if a post has 10 comments, and the user clicks to see more comments, the user will get to see 10 more comments
    //if the commentId has a value, it will give 10 more replies of a specific comment.
    //@PreAuthorize(("@canPartakeInCommunity.userCanViewPost(#authorization, #postId)"))
    @GetMapping("/{postId}/{page}/{parentCommentId}")
    public List<CommentDTO> getIntervallOfComments(
            @PathVariable Long postId,
            @PathVariable Integer page,
            @PathVariable Long parentCommentId
    ) {
        if (parentCommentId != 0)
            return commentService.getReplyInterval(parentCommentId, page);
        return commentService.getCommentInterval(postId, page);
    }


    //@PreAuthorize(("@canPartakeInCommunity.userCanViewPost(#authorization, #postId)"))
    @GetMapping("/not_logged_in/{postId}/{page}/{parentCommentId}")
    public List<CommentDTO> getIntervallOfCommentsWithoutToken(
            @PathVariable Long postId,
            @PathVariable Integer page,
            @PathVariable Long parentCommentId
    ) {
        if (parentCommentId != 0)
            return commentService.getReplyInterval(parentCommentId, page);
        return commentService.getCommentInterval(postId, page);
    }

}
