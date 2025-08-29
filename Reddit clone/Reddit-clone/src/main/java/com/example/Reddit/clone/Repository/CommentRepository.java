package com.example.Reddit.clone.Repository;

import com.example.Reddit.clone.Entity.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c WHERE c.parent.id = :parentCommentId")
    List<Comment> getAllCommentsOfPostWithId(@Param("parentCommentId") Long parentCommentId);

    @Query(value = "SELECT c FROM Comment c WHERE c.parent.id = :parentCommentId ORDER BY c.createdAt DESC")
    public List<Comment> getReplyIntervall(@Param("parentCommentId") Long parentCommentId, PageRequest pageRequest);


    // SELECT * FROM reddit_clone.comment WHERE post_id = 14;

    @Query(value = "SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL ORDER BY c.createdAt DESC")
    List<Comment> getCommentIntervall(@Param("postId") Long postId, PageRequest pageRequest);

    @Query(value = "SELECT c FROM Comment c WHERE c.parent IS NULL AND c.post.id = :postId ORDER BY c.id DESC")
    Comment getLastChildOfPost(Long postId, PageRequest pageRequest);

    @Query(value = "SELECT c FROM Comment c WHERE c.parent.id = :parentCommentId ORDER BY c.id DESC")
    Comment getLastChild(Long parentCommentId, PageRequest pageRequest);

    @Query(value = "SELECT COUNT(c) FROM Comment c WHERE c.parent.id = :parentCommentId")
    Integer getAmountOfChildrenOfCommentWithId(Long parentCommentId);

    @Query("SELECT c FROM Comment c WHERE c.user.username LIKE :username AND c.user IS NOT NULL")
    Set<Comment> getCommentsOfUserOrderByCreatedAt(@Param("username") String username, PageRequest of);

    boolean existsByTitle(String title);
}
