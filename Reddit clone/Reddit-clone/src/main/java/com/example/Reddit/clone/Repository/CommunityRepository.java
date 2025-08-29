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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {


    boolean existsByTitle(String title);

    @Modifying
    @Query(value = "INSERT INTO user_community (user_id, community_id) VALUES (:userId, :communityId)", nativeQuery = true)
    public void addUserToCommunity(@Param("userId") Long userId, @Param("communityId") Long communityId);


    Optional<Community> findByTitle(String title);


    @Query("SELECT c FROM Community c WHERE c.title LIKE :title")
    Community getComuntiyByTitle(@Param("title") String title);



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


    @Query("SELECT p FROM Post p WHERE p.community IN :communities ORDER BY p.createdAt DESC")
    List<Post> getPostsOfCommunities(@Param("communities") Set<Community> communities, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO user_community_admin (user_id, community_id) VALUES (:userId, :communityId)", nativeQuery = true)
    public void addAdminToCommunity(@Param("userId") Long userId, @Param("communityId") Long communityId);

    @Modifying
    @Query(value = "INSERT INTO user_community_moderator (user_id, community_id) VALUES (:userId, :communityId)", nativeQuery = true)
    public void addModToCommunity(@Param("userId") Long userId, @Param("communityId") Long communityId);

    @Query("SELECT c.members FROM Community c WHERE c.title LIKE :communityName")
    Set<User> findMembersOfCommunity(String communityName);


    @Modifying
    @Query(value = "DELETE FROM user_community_moderator WHERE user_id = :userId AND community_id = :communityId", nativeQuery = true)
    public void removeModeratorPowers(@Param("userId") Long userId, @Param("communityId") Long communityId);

    @Modifying
    @Query(value = "DELETE FROM user_community_admin WHERE user_id = :userId AND community_id = :communityId", nativeQuery = true)
    public void removeAdminPowers(@Param("userId") Long userId, @Param("communityId") Long communityId);

    @Modifying
    @Query(value = "DELETE FROM user_community WHERE user_id = :userId AND community_id = :communityId", nativeQuery = true)
    public void unsubscribeUserFromCommunity(@Param("userId") Long userId, @Param("communityId") Long communityId);

    @Query("SELECT c.administrators FROM Community c WHERE c.title LIKE :communityName")
    Set<User> findAdminsOfCommunity(String communityName);

    @Query("SELECT c.moderators FROM Community c WHERE c.title LIKE :communityName")
    Set<User> findModsOfCommunity(String communityName);

    @Modifying
    @Query(value = "INSERT INTO user_community_banned (user_id, community_id) VALUES (:userId, :communityId)", nativeQuery = true)
    void banUserFromCommunity(Long userId, Long communityId);


    @Modifying
    @Query("DELETE FROM Community c WHERE c.id = :communityId")
    void deleteCommunity(@Param("communityId") Long communityId);
}
