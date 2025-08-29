package com.example.Reddit.clone.Repository;

import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long postId);



    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllByOrderByCreatedAtAsc();


    @Query("SELECT p FROM Post p WHERE p.createdAt <= :startDate AND p.id <> :idOfLastPost ORDER BY p.createdAt DESC")
    public List<Post> find10LaterPostAfterPost(
            @Param("startDate") LocalDateTime startDate,
            @Param("idOfLastPost") Long idOfLastPost,
            Pageable pageable);














    //må ha med noe limit type 10 ting, kan finne ut hvordan implementere dette senere
    @Query("SELECT p FROM Post p WHERE " +
            "(:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:earlyDate IS NULL OR p.createdAt >= :earlyDate) " +
            "AND (:latestDate IS NULL OR p.createdAt <= :latestDate)")
    List<Post> searchPosts(
            @Param("title") String title,
            @Param("earlyDate") LocalDateTime earlyDate,
            @Param("latestDate") LocalDateTime latestDate
    );


    @Query("SELECT p FROM Post p WHERE " +
            "p.community.title = :communityTitle " +
            "ORDER BY p.createdAt DESC")
    public List<Post> find20LaterPostAfterPost(@Param("communityTitle") String communityTitle, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE " +
            "p.community.title = :communityTitle " +
            "ORDER BY p.createdAt DESC")
    public List<Post> findLaterPostAfterPost(@Param("communityTitle") String communityTitle);


    @Query("SELECT p FROM Post p WHERE p.user.username LIKE :username")
    public List<Post> getPostsOfUserOrderByCreatedAt(@Param("username") String username, PageRequest of);

    boolean existsByTitle(String title);



    @Query("SELECT p FROM Post p WHERE p.community.communityType = 'PUBLIC' ORDER BY createdAt DESC")
    List<Post> getLatestsPostsOfPublicCommunities(PageRequest of);



        /*
        Hent ut communities der bruker er medlem - Dette er en liste

         */


}