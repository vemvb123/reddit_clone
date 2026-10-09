package com.example.Reddit.clone.Services;

import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.DTO.ResponseTextType;
import com.example.Reddit.clone.DTO.UserDTO;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.Message;
import com.example.Reddit.clone.Entity.MessageTopic;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Exception.MessageException;
import com.example.Reddit.clone.Exception.MessageExceptionMessage;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Mapper.CommunityMapper;
import com.example.Reddit.clone.Mapper.UserMapper;
import com.example.Reddit.clone.Repository.MessageRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${images.path}")
    public String pathToSaveImages;

    final private UserRepository userRepository;
    final private MessageRepository messageRepository;

    final private JwtService jwtService;

    final private UserMapper userMapper;
    final private CommunityMapper communityMapper;



    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        return userMapper.entityToDto(user);
    }


    public ResponseText setImage(MultipartFile file, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (!file.isEmpty()) {
            try {
                if (!new File(pathToSaveImages).exists())
                    new File(pathToSaveImages).mkdir();

                String orgName = file.getOriginalFilename();
                String filePath = pathToSaveImages + orgName;
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
        return new ResponseText(ResponseTextType.FILE_SAVED);

    }


    public UserDTO getUserByToken() {
        User user = userRepository.findByUsername(SecurityUtils.getUsername())
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        return userMapper.entityToDto(user);
    }


    @Transactional
    public List<CommunityDTO> getCommunitiesUserIsMemberOf(String username) {
        Set<Community> communities = userRepository.findCommunitiesUserIsMemberOf(username);
        return communities.stream()
                .map(community -> communityMapper.entityToDto(community))
                .toList();
    }


    public ResponseText sendFriendRequestFromUser(String toUsername) {
        String fromUsername = SecurityUtils.getUsername();
        User fromUser = userRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        //se om ikke person allerede har sendt/fått request
        if (messageRepository.findFriendRequestFromUserToUser(fromUsername, toUsername) != null)
            throw new MessageException(MessageExceptionMessage.FRIEND_REQUEST_ALREADY_SENT);

        Message message = new Message();
        message.setMessageTopic(MessageTopic.NewFriendRequest);
        message.setSeen(false);
        message.setToUser(toUser);
        message.setFromUser(fromUser);
        message.setEventHappendAt(LocalDateTime.now());
        messageRepository.save(message);
        return new ResponseText("Sent request successfully");
    }


    private void makeUsersBecomeFriends(User user1, User user2) {
        userRepository.addUserAsFriendToThisUser(user1.getId(), user2.getId());
        userRepository.addUserAsFriendToThisUser(user2.getId(), user1.getId());
    }


    private void deleteFriendRequestsBetweenUsers(User user1, User user2) {
        userRepository.deleteFriendRequestsFromUserToUser(user1.getId(), user2.getId());
        userRepository.deleteFriendRequestsFromUserToUser(user2.getId(), user1.getId());
    }


    public ResponseText acceptFriendRequest(String fromUsername) {
        User fromUser = userRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        String toUsername = SecurityUtils.getUsername();
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (messageRepository.findFriendRequestFromUserToUser(fromUsername, toUsername) == null)
            throw new NotFoundException(NotFound.FRIEND_REQUEST);

        makeUsersBecomeFriends(toUser, fromUser);
        deleteFriendRequestsBetweenUsers(toUser, fromUser);
        return new ResponseText("Request accepted successfully");
    }



    public ResponseText changeOthersCanSeePostsAndComments(Integer allowedToSeePosts, Integer allowedToSeeComments) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (allowedToSeePosts == 1)
            user.setOtherUsersCanSeePosts(true);
        else if (allowedToSeePosts == 0)
            user.setOtherUsersCanSeePosts(false);

        if (allowedToSeeComments == 1)
            user.setOtherUsersCanSeeComments(true);
        else if (allowedToSeeComments == 0)
            user.setOtherUsersCanSeeComments(false);

        userRepository.save(user);
        return new ResponseText("Changed who can see posts and comments of user");
    }

    public ResponseText setWallpaper(MultipartFile file, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (!file.isEmpty()) {
            try {
                if (!new File(pathToSaveImages).exists())
                    new File(pathToSaveImages).mkdir();

                String orgName = file.getOriginalFilename();
                String filePath = pathToSaveImages + orgName;
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
        return new ResponseText(ResponseTextType.FILE_SAVED);
    }

}
