package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.DTO.ResponseTextType;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.UserException;
import com.example.Reddit.clone.Mapper.CommentMapper;
import com.example.Reddit.clone.Repository.CommentRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class CommentService {


    @Value("${images.path}")
    public String pathToSaveImages;

    private UserRepository userRepository;
    private PostRepository postRepository;
    private MessageService messageService;
    private CommentRepository commentRepository;

    private CommentMapper commentMapper;


    public List<CommentDTO> getReplyInterval(Long parentCommentId, int page) {
        Comment lastChild = commentRepository.getLastChild(parentCommentId, PageRequest.of(0,1));
        List<Comment> replyInterval = commentRepository.getReplyInterval(parentCommentId, PageRequest.of(page, 10));

         return replyInterval.stream()
                .map(reply -> commentMapper.entityToDTO(
                        reply,
                        Objects.equals(lastChild.getId(), reply.getId()),
                        false,
                        commentRepository.getAmountOfChildrenOfCommentWithId(reply.getId()) != 0
                )).toList();
    }


    public List<CommentDTO> getCommentInterval(Long postId, int page) {
        Comment lastChild = commentRepository.getLastChildOfPost(postId, PageRequest.of(0,1));
        List<Comment> commentInterval = commentRepository.getCommentInterval(postId, PageRequest.of(page, 10));

        return commentInterval.stream()
                .map(comment -> commentMapper.entityToDTO(
                         comment,
                        Objects.equals(comment.getId(), lastChild.getId()),
                        true,
                        commentRepository.getAmountOfChildrenOfCommentWithId(comment.getId()) > 0
                )).toList();
   }


    public CommentDTO saveComment(CommentDTO commentDTO) {
        User user = userRepository.findByUsername(SecurityUtils.getUsername())
                .orElseThrow(UserException::new);

        Comment parentComment = null;
        if (commentDTO.parentCommentId() != null)
            parentComment = commentRepository.findById(commentDTO.parentCommentId())
                    .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));

        Post post = postRepository.findById(commentDTO.postId())
                .orElseThrow(() -> new NotFoundException(NotFound.POST));

        Comment comment = commentMapper.dtoToEntity(commentDTO, post, parentComment, user);
        Comment savedComment = commentRepository.save(comment);

        //making message to user the comment is meant for
        if (parentComment != null)
            messageService.saveMessageNewReplyToComment(savedComment);
        else
            messageService.saveMessageNewReplyToPost(savedComment);

        return commentMapper.entityToDTO(comment, null, comment.getIsPrimeComment(), !comment.getChildren().isEmpty());
    }


    public void setImage(MultipartFile file, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));

        if (comment.getPathToImage() != null)
            comment.setLastUpdated(LocalDateTime.now());

        if (!file.isEmpty()) {
            try {
                if (!new File(pathToSaveImages).exists()) {
                    new File(pathToSaveImages).mkdir();
                }

                String orgName = file.getOriginalFilename();
                String filePath = pathToSaveImages + orgName;
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
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));
        return commentMapper.entityToDTO(comment, null, comment.getIsPrimeComment(), !comment.getChildren().isEmpty());
    }


    public ResponseText deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMENT));

        if (commentRepository.getAmountOfChildrenOfCommentWithId(commentId) == 0)
            commentRepository.deleteById(comment.getId());
        else {
            comment.setUser(null);
            comment.setDescription("This comment has been deleted");
            comment.setPathToImage(null);
            comment.setTitle("...");
            commentRepository.save(comment);
        }
        return new ResponseText(ResponseTextType.DELETED);
    }


    public List<CommentDTO> get20LatestCommentsOfUserLoggedIn(String username, Integer page) {
        User user = userRepository.findByUsername(SecurityUtils.getUsername())
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Set<Comment> comments = commentRepository.getCommentsOfUserOrderByCreatedAt(user.getUsername(), PageRequest.of(page, 20));

        Set<Community> userCommunities = userRepository.findCommunitiesUserIsMemberOf(SecurityUtils.getUsername());
        return comments.stream()
                .filter(comment ->
                        comment.getUser() == null
                                || comment.getPost().getCommunity().getCommunityType() == CommunityType.PUBLIC
                                || userCommunities.contains(comment.getPost().getCommunity())
                )
                .map(comment ->
                        commentMapper.entityToDTO(comment, false, false, false)
                )
                .toList();
    }


    public List<CommentDTO> get20LatestCommentsOfUserNotLoggedIn(String username, Integer page) {
        User user = userRepository.findByUsername(username )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Set<Comment> comments = commentRepository.getCommentsOfUserOrderByCreatedAt(user.getUsername(), PageRequest.of(page, 20));

        return comments.stream()
        .filter(comment -> comment.getUser() == null ||
                comment.getPost().getCommunity().getCommunityType() == CommunityType.PUBLIC
        )
        .map(comment -> commentMapper.entityToDTO(comment, false, false, false))
        .toList();
   }

}