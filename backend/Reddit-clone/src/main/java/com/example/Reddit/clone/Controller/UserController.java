package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CheckIfMethodCalled;
import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.DTO.UserDTO;
import com.example.Reddit.clone.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {

    //@Autowired
    //private OwnerCheck ownerCheck;

    //@Autowired
    //private CanPartakeInCommunity canPartakeInCommunity;
    private CheckIfMethodCalled checkIfMethodCalled;
    private UserService userService;


    //Todo: Bare administrator kan gjøre dette ..
    @PreAuthorize(("@ownerCheck.usernameIsSameAsToken(#username)"))
    @PostMapping("/wallpaper/{username}")
    public ResponseText setWallpaper(
            @RequestParam("file") MultipartFile file,
            @PathVariable String username
    ) {
        return userService.setWallpaper(file, username);
    }


    //bruker sender friend request
    //mottaker aksepterer request
    //kan kun bli venner hvis: mottaker har akseptert den vennerequesten som avsenderen har sendt.
    // En bruker sender en friend request. Dette lagres som en entitet: toUser_id, fromUser_id, accepted
    // Denne entiteten gjøres om til en message når toUser går inn på siden
    // hvis toUser avslår request, slettes forespørselen
    // hvis toUser godtar request, slettes forespørselen, og vennskapet blit opprettet
    @PostMapping("/send_friend_request/{toUsername}") //toUsername - Brukernavn til bruker som får request
    public ResponseText sendFriendRequest(@PathVariable String toUsername)
    {
        return userService.sendFriendRequestFromUser(toUsername);
    }

    @PostMapping("/accept_friend_request/{fromUsername}") //fromUsername - Brukernavn til bruker som request er fra
    public ResponseText acceptFriendRequest(@PathVariable String fromUsername)
    {
        return userService.acceptFriendRequest(fromUsername);
    }


    @PostMapping("/see_posts_and_comments/{allowedToSeePosts}/{allowedToSeeComments}") //fromUsername - Brukernavn til bruker som request er fra
    public ResponseText changeOthersCanSeePostsAndComments(
            @PathVariable Integer allowedToSeePosts,
            @PathVariable Integer allowedToSeeComments
    ) {
        return userService.changeOthersCanSeePostsAndComments(allowedToSeePosts, allowedToSeeComments);
    }


    //Todo: post - save file path
    //@PreAuthorize(("@ownerCheck.userOwnsImage(#authorization, #username)"))
    @PostMapping("/profile_image/{username}")
    public ResponseText setProfileImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable String username
    ) {
        return userService.setImage(file, username);
    }


    @GetMapping("/{username}")
    public UserDTO getUserByUsername(
            @PathVariable String username
    ) {
        return userService.getUserByUsername(username);
    }


    @GetMapping("/token")
    public UserDTO getUserByToken() {
        return userService.getUserByToken();
    }


    @GetMapping("/communities_of_user/{username}")
    public List<CommunityDTO> getCommunitiesUserIsMemberOf(
            @PathVariable String username
    ) {
        return userService.getCommunitiesUserIsMemberOf(username);
    }


}
