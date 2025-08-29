package com.example.Reddit.clone.Repository;

import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    @Query("SELECT u.communities FROM User u WHERE u.username = :username")
    Set<Community> findCommunitiesByUsername(@Param("username") String username);

    @Query("SELECT u.communities FROM User u WHERE u.username LIKE :username")
    Set<Community> findCommunitiesUserIsMemberOf(@Param("username") String username);



    @Query("SELECT u.moderatorOnCommunities FROM User u WHERE u.username LIKE :username")
    Set<Community> findCommunitiesUserIsModeratorOf(@Param("username") String username);

    @Query("SELECT u.administratorOnCommunities FROM User u WHERE u.username LIKE :username")
    Set<Community> findCommunitiesUserIsAdministratorOf(@Param("username") String username);


    @Modifying
    @Query(value = "INSERT INTO friends (user_id, friend_id) VALUES (:userId, :friendId)", nativeQuery = true)
    void addUserAsFriendToThisUser(Long userId, Long friendId);


    @Query("SELECT u.friends FROM User u WHERE u.id = :userId")
    Set<User> getFriendsOfUser(@Param("userId") Long userId);





    @Modifying
    @Query("DELETE FROM Message m WHERE m.fromUser.id = :fromUserId AND m.toUser.id = :toUserId")
    void deleteFriendRequestsFromUserToUser(@Param("fromUserId") Long fromUserid, @Param("toUserId") Long toUserid);


    @Query("SELECT u.bannedFromCommunities FROM User u WHERE u.username LIKE :username")
    Set<Community> getCommunitiesBannedFrom(@Param("username") String username);

    boolean existsByUsername(String username);

    //@Query("SELECT u.messagesFrom m FROM User u WHERE m.communityRequestingToJoin IS NOT NULL AND m.communityRequestingToJoin.title LIKE :communityName AND m.fromUser.username LIKE :usernameRequestingToJoin")
    //Set<Message> findRequestsToJoinCommunities(String communityName, String usernameRequestingToJoin);
}

