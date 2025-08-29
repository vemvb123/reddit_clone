package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Repository.CommentRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentService {



    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MessageService messageService;




    @Autowired
    private CommentRepository commentRepository;











    public List<CommentDTO> getReplyIntervall(Long parentCommentId, int page) {
        Comment lastChild = commentRepository.getLastChild(parentCommentId, PageRequest.of(0,1));

        List<Comment> replyIntervall = commentRepository.getReplyIntervall(parentCommentId, PageRequest.of(0, 10));

        List<CommentDTO> returnDTOReplies = new ArrayList<>();
        for (Comment reply : replyIntervall)
            returnDTOReplies.add(new CommentDTO().mapCommentToDTO
                    (
                            reply,
                            Objects.equals(lastChild.getId(), reply.getId()),
                            false,
                            commentRepository.getAmountOfChildrenOfCommentWithId(reply.getId()) != 0
                    )
            );

        return returnDTOReplies;
    }





    public List<CommentDTO> getCommentIntervall(Long postId, int page) {
        Comment lastChild = commentRepository.getLastChildOfPost(postId, PageRequest.of(0,1));

        List<Comment> commentIntervall = commentRepository.getCommentIntervall(postId, PageRequest.of(page, 10));

        List<CommentDTO> returnDTOComments = new ArrayList<>();
        for (Comment comment : commentIntervall)
            returnDTOComments.add(new CommentDTO().mapCommentToDTO
                    (
                            comment,
                            Objects.equals(comment.getId(), lastChild.getId()),
                            true,
                            commentRepository.getAmountOfChildrenOfCommentWithId(comment.getId()) > 0
                    )
            );

        return returnDTOComments;
    }




    public Comment saveComment(CommentDTO commentDTO, String authorization) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Comment parentComment = null;
        if (commentDTO.getParentCommentId() != null)
            parentComment = commentRepository.getById(commentDTO.getParentCommentId());

        Post post = postRepository.getById(commentDTO.getPostId());


        Comment comment = new Comment();
        boolean idIsNull = false;
        if (commentDTO.getId() != null) {
            comment.setId(commentDTO.getId());
            comment.setLastUpdated(LocalDateTime.now());
            comment.setCreatedAt(commentDTO.getCreatedAt());

        }
        else {
            idIsNull = true;
            comment.setCreatedAt(LocalDateTime.now());
        }

        comment.setPathToImage(commentDTO.getPathToImage());
        comment.setDescription(commentDTO.getDescription());
        comment.setTitle(commentDTO.getTitle());
        comment.setPost(post);
        comment.setParent(parentComment);
        comment.setChildren(new HashSet<>());
        comment.setIsPrimeComment(commentDTO.getIsPrimeComment());

        User fetchedUser = userRepository.getById(user.getId());
        comment.setUser(fetchedUser);




        Comment savedComment = commentRepository.save(comment);

        //making message to user the comment is meant for
        System.out.println(parentComment + " herjakomigjen");
        if (parentComment != null)
            messageService.saveMessageNewReplyToComment(savedComment);
        else
            messageService.saveMessageNewReplyToPost(savedComment);





        return savedComment;
    }

    // når man replier til en kommentar, skal den som blir repliet til få vite om det
    // da skal det si at replien kom fra en kommentar, til din egen kommentar
    // det samme for post XXXXXXXXXXXX






    public void setImage(MultipartFile file, Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        if (comment.getPathToImage() != null)
            comment.setLastUpdated(LocalDateTime.now());

        if (!file.isEmpty()) {
            try {
                if (!new File(FileService.pathToSaveImages).exists()) {
                    new File(FileService.pathToSaveImages).mkdir();
                }

                String orgName = file.getOriginalFilename();
                String filePath = FileService.pathToSaveImages + orgName;
                filePath = FileService.makeOriginalFileName(filePath);
                File dest = new File(filePath);
                file.transferTo(dest);



                if (new File(filePath).exists()) {
                    comment.setPathToImage(file.getOriginalFilename());
                    commentRepository.save(comment);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public CommentDTO getComment(Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        return new CommentDTO().mapCommentToDTO(comment, null, comment.getIsPrimeComment(), comment.getChildren().size() > 0);
    }


    public void deleteComment(Long commentId) {
        System.out.println("ssd");
        Comment comment = commentRepository.getById(commentId);
        System.out.println("ssdf");
        if (commentRepository.getAmountOfChildrenOfCommentWithId(commentId) == 0) {
            System.out.println("ss");
            commentRepository.deleteById(comment.getId());
        } else {
        System.out.println("ssdfc");

        comment.setUser(null);
        comment.setDescription("This comment has been deleted");
        comment.setPathToImage(null);
        comment.setTitle("...");
        commentRepository.save(comment);
        System.out.println("ssdfd");




        }
    }




    public List<CommentDTO> get20LatestCommentsOfUser(String username, Integer page, String authorization) {
        String usernameLooking = jwtService.extractUsername(authorization);
        User userLooking = userRepository.findByUsername(usernameLooking) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameLooking));


        User user = userRepository.findByUsername(username ) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username ));

        Set<Comment> comments = commentRepository.getCommentsOfUserOrderByCreatedAt(user.getUsername(), PageRequest.of(page, 20));

        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUser() != null)
                if (!(comment.getPost().getCommunity().getCommunityType() == CommunityType.PUBLIC))
                    if (!userRepository.findCommunitiesUserIsMemberOf(usernameLooking).contains(comment.getPost().getCommunity()))
                        continue;

                commentDTOs.add( new CommentDTO().mapCommentToDTO(comment, false, false, false) );
        }

        return commentDTOs;
    }





    public List<CommentDTO> get20LatestCommentsOfUser(String username, Integer page) {


        User user = userRepository.findByUsername(username ) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username ));

        Set<Comment> comments = commentRepository.getCommentsOfUserOrderByCreatedAt(user.getUsername(), PageRequest.of(page, 20));

        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getUser() != null)
                if (!(comment.getPost().getCommunity().getCommunityType() == CommunityType.PUBLIC))
                    continue;

            commentDTOs.add( new CommentDTO().mapCommentToDTO(comment, false, false, false) );
        }

        return commentDTOs;
    }
}
