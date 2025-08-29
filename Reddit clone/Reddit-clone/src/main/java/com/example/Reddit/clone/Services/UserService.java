package com.example.Reddit.clone.Services;

import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.UserDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Repository.CommentRepository;
import com.example.Reddit.clone.Repository.MessageRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtService jwtService;

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));
        return new UserDTO().mapObjectToDTO(user);
    }


    public void setImage(MultipartFile file, String username) {


        User user = userRepository.findByUsername(username) .orElseThrow(() -> new RuntimeException("User not found"));

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
                    user.setPathToProfileImage(file.getOriginalFilename());
                    userRepository.save(user);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }


    }


    public UserDTO getUserByToken(String authorization) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow();

        return new UserDTO().mapObjectToDTO(user);

    }






    @Transactional
    public List<CommunityDTO> getCommunitiesUserIsMemberOf(String username) {

        System.out.println("1");
        Set<Community> communities = userRepository.findCommunitiesUserIsMemberOf(username);
        System.out.println(communities);
        System.out.println("2");

        List<CommunityDTO> communityDTOs = new ArrayList<>();
        for (Community community : communities)
            communityDTOs.add( new CommunityDTO().mapObjectToDTO(community) );
        System.out.println("3");



        return communityDTOs;
    }


    @Autowired
    CommentRepository commentRepository;

    public void sendFriendRequestFromUser(String toUsername, String authorization) {
        String fromUsername = jwtService.extractUsername(authorization);
        User fromUser = userRepository.findByUsername(fromUsername).orElseThrow();
        System.out.println("der1");

        User toUser = userRepository.findByUsername(toUsername).orElseThrow();
        System.out.println("der2");

        //se om ikke person allerede har sendt/fått request
        if (messageRepository.findFriendRequestFromUserToUser(fromUsername, toUsername) != null)
            ExceptionUtils.friendRequestHasAlreadyBeenSent(toUsername, fromUsername);
        System.out.println("der3");

        Message message = new Message();
        message.setMessageTopic(MessageTopic.NewFriendRequest);
        message.setSeen(false);
        message.setToUser(toUser);
        message.setFromUser(fromUser);
        message.setEventHappendAt(LocalDateTime.now());
        // message.setComment(commentRepository.findById(155L).orElseThrow());

        // VIKTIG: Hvis det mases om et constraint, så har det med at messagetopic er ute av sync med hvor mange beskjeder som finnes
        // da må du muligens slette kolonnen for messagetopic, så la springboot gjenlage kolonnen

        System.out.println("der4");

        System.out.println(message.toString());
        messageRepository.save(message);
        System.out.println("der5");
    }


    @Transactional
    private void makeUsersBecomeFriends(User user1, User user2) {
        userRepository.addUserAsFriendToThisUser(user1.getId(), user2.getId());
        userRepository.addUserAsFriendToThisUser(user2.getId(), user1.getId());
    }
    @Transactional
    private void deleteFriendRequestsBetweenUsers(User user1, User user2) {
        userRepository.deleteFriendRequestsFromUserToUser(user1.getId(), user2.getId());
        userRepository.deleteFriendRequestsFromUserToUser(user2.getId(), user1.getId());
    }
    @Transactional
    public void acceptFriendRequest(String fromUsername, String authorization) {
        User fromUser = userRepository.findByUsername(fromUsername).orElseThrow();

        String toUsername = jwtService.extractUsername(authorization);
        User toUser = userRepository.findByUsername(toUsername).orElseThrow();

        if (messageRepository.findFriendRequestFromUserToUser(fromUsername, toUsername) == null)
            ExceptionUtils.noExistingFriendRequestFromUserToUser(fromUsername, toUsername);

        makeUsersBecomeFriends(toUser, fromUser);
        deleteFriendRequestsBetweenUsers(toUser, fromUser);
    }



    public void changeOthersCanSeePostsAndComments(Integer allowedToSeePosts, Integer allowedToSeeComments, String authorization) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));

        if (allowedToSeePosts == 1)
            user.setOtherUsersCanSeePosts(true);
        else if (allowedToSeePosts == 0)
            user.setOtherUsersCanSeePosts(false);

        if (allowedToSeeComments == 1)
            user.setOtherUsersCanSeeComments(true);
        else if (allowedToSeeComments == 0)
            user.setOtherUsersCanSeeComments(false);

        userRepository.save(user);

    }

    public void setWallpaper(MultipartFile file, String username) {

        User user = userRepository.findByUsername(username) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));

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
                    user.setPathToWallpaperImage(file.getOriginalFilename());
                    userRepository.save(user);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }





    }
}
