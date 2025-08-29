package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CanPartakeInCommunity;
import com.example.Reddit.clone.ACL.OwnerCheck;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.Comment;
import com.example.Reddit.clone.Repository.CommentRepository;
import com.example.Reddit.clone.Services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
@EnableAutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CommentController {

    private final String origin = "http://localhost:3000";

    @Autowired
    private CommentService commentService;

    @Autowired
    private OwnerCheck ownerCheck;






    @CrossOrigin(origins = origin)
    @GetMapping("/get_comment/{commentId}")
    public ResponseEntity<CommentDTO> getComment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId
    ) {
        CommentDTO commentDTO = commentService.getComment(commentId);

        return ResponseEntity.ok().body(commentDTO);


    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.canDeleteComment(#authorization, #commentId)"))
    @PostMapping("/delete_comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId
    ) {
        System.out.println("controller triggered");
        commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");


    }

    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersComments(#username)"))
    @GetMapping("/get_20_latets_comments_of_user/{page}/{username}")
    public ResponseEntity<List<CommentDTO>> getLatestCommentsOfUser(
            @RequestHeader("Authorization") String authorization,
            @PathVariable String username,
            @PathVariable Integer page
    )
    {
        System.out.println("hallo");
        List<CommentDTO> latestComments = commentService.get20LatestCommentsOfUser(username, page, authorization);

        return ResponseEntity.ok().body(latestComments);
    }

    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanViewOtherUsersComments(#username)"))
    @GetMapping("/get_20_latets_comments_of_user_not_logged_in/{page}/{username}")
    public ResponseEntity<List<CommentDTO>> getLatestCommentsOfUser(
            @PathVariable String username,
            @PathVariable Integer page
    )
    {
        System.out.println("SADSADASDAASDASD");
        List<CommentDTO> latestComments = commentService.get20LatestCommentsOfUser(username, page);

        return ResponseEntity.ok().body(latestComments);
    }

    //Todo: Huh?
    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanMakeThisComment(#authorization, #commentDTO)"))
    @PostMapping("/saveComment")
    public ResponseEntity<CommentDTO> saveComment(
            @RequestBody CommentDTO commentDTO,
            @RequestHeader("Authorization") String authorization)
    {

        System.out.println("incommentcontroller");
        Comment comment = commentService.saveComment(commentDTO, authorization);
        CommentDTO responseCommentDTO = new CommentDTO();
        responseCommentDTO.setId(comment.getId());
        responseCommentDTO.setPostId(comment.getId());
        if (comment.getParent() != null)
            responseCommentDTO.setParentCommentId(comment.getParent().getId());
        responseCommentDTO.setDescription(comment.getDescription());
        responseCommentDTO.setTitle(comment.getTitle());
        responseCommentDTO.setUserId(comment.getUser().getId());

        return ResponseEntity.ok().body(responseCommentDTO);
    }



    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userOwnsComment(#authorization, #commentId)"))
    @PostMapping("/setCommentImage/{commentId}")
    public ResponseEntity<String> setWallpaper(@RequestParam("file") MultipartFile file, @PathVariable Long commentId, @RequestHeader("Authorization") String authorization)
    {

        commentService.setImage(file, commentId);

        return ResponseEntity.status(HttpStatus.OK).body("File saved successfully");
    }



    //will return 10 comments from a certain point
    //if a post has 10 comments, and the user clicks to see more comments, the user will get to see 10 more comments
    //if the commentId has a value, it will give 10 more replies of a specific comment.

    @CrossOrigin(origins = origin)
    //@PreAuthorize(("@canPartakeInCommunity.userCanViewPost(#authorization, #postId)"))
    @GetMapping("/getIntervallOfComments/{postId}/{page}/{parentCommentId}")
    public ResponseEntity<List<CommentDTO>> getIntervallOfComments(
            @PathVariable Long postId,
            @PathVariable Integer page,
            @PathVariable Long parentCommentId,
            @RequestHeader String authorization
            )
    {


        if (parentCommentId != 0) {
            return ResponseEntity.ok().body(commentService.getReplyIntervall(parentCommentId, page));
        }

        return ResponseEntity.ok().body(commentService.getCommentIntervall(postId, page));
    }

    @CrossOrigin(origins = origin)
    //@PreAuthorize(("@canPartakeInCommunity.userCanViewPost(#authorization, #postId)"))
    @GetMapping("/getIntervallOfCommentsWithoutToken/{postId}/{page}/{parentCommentId}")
    public ResponseEntity<List<CommentDTO>> getIntervallOfCommentsWithoutToken(
            @PathVariable Long postId,
            @PathVariable Integer page,
            @PathVariable Long parentCommentId
    )
    {


        if (parentCommentId != 0) {
            return ResponseEntity.ok().body(commentService.getReplyIntervall(parentCommentId, page));
        }

        return ResponseEntity.ok().body(commentService.getCommentIntervall(postId, page));
    }





}
