package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CheckIfMethodCalled;
import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.UserDTO;
import com.example.Reddit.clone.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.Reddit.clone.ACL.CanPartakeInCommunity;
import com.example.Reddit.clone.ACL.OwnerCheck;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {


    final String origin = "http://localhost:3000";

    //@Autowired
    //private OwnerCheck ownerCheck;

    //@Autowired
    //private CanPartakeInCommunity canPartakeInCommunity;

    @Autowired
    private CheckIfMethodCalled checkIfMethodCalled;



    @Autowired
    private UserService userService;


    //Todo: Bare administrator kan gjøre dette
    @PreAuthorize(("@ownerCheck.usernameIsSameAsToken(#username, #authorization)"))
    @PostMapping("/setWallpaper/{username}")
    public ResponseEntity<String> setWallpaper(@RequestParam("file") MultipartFile file,
                                               @PathVariable String username,
                                               @RequestHeader("Authorization") String authorization
    ) {

        System.out.println("her " + authorization);
        userService.setWallpaper(file, username);

        return ResponseEntity.status(HttpStatus.OK).body("File saved successfully");
    }





    //bruker sender friend request
    //mottaker aksepterer request
    //kan kun bli venner hvis: mottaker har akseptert den vennerequesten som avsenderen har sendt.
    // En bruker sender en friend request. Dette lagres som en entitet: toUser_id, fromUser_id, accepted
    // Denne entiteten gjøres om til en message når toUser går inn på siden
    // hvis toUser avslår request, slettes forespørselen
    // hvis toUser godtar request, slettes forespørselen, og vennskapet blit opprettet
    @CrossOrigin(origins = origin)
    @PostMapping("/send_friend_request/{toUsername}") //toUsername - Brukernavn til bruker som får request
    public ResponseEntity<String> sendFriendRequest(
            @PathVariable String toUsername,
            @RequestHeader("Authorization") String authorization)
    {

        System.out.println("herjaderja");
        userService.sendFriendRequestFromUser(toUsername, authorization);

        return ResponseEntity.status(HttpStatus.OK).body("Sent request successfully");
    }

    @CrossOrigin(origins = origin)
    @PostMapping("/accept_friend_request/{fromUsername}") //fromUsername - Brukernavn til bruker som request er fra
    public ResponseEntity<String> acceptFriendRequest(@PathVariable String fromUsername, @RequestHeader("Authorization") String authorization)
    {

        userService.acceptFriendRequest(fromUsername, authorization);

        return ResponseEntity.status(HttpStatus.OK).body("Request accepted successfully");
    }



    @CrossOrigin(origins = origin)
    @PostMapping("/change_others_allowed_to_see_users_posts_and_comments/{allowedToSeePosts}/{allowedToSeeComments}") //fromUsername - Brukernavn til bruker som request er fra
    public ResponseEntity<String> changeOthersCanSeePostsAndComments(
            @PathVariable Integer allowedToSeePosts,
            @PathVariable Integer allowedToSeeComments,
            @RequestHeader("Authorization") String authorization)
    {
        userService.changeOthersCanSeePostsAndComments(allowedToSeePosts, allowedToSeeComments, authorization);

        return ResponseEntity.status(HttpStatus.OK).body("Changed who can see posts and comments of user");
    }



    //Todo: post - save file path
    @CrossOrigin(origins = origin)
    //@PreAuthorize(("@ownerCheck.userOwnsImage(#authorization, #username)"))
    @PostMapping("/set_profile_image_for_user/{username}")
    public ResponseEntity<String> setProfileImage(@RequestParam("file") MultipartFile file, @PathVariable String username, @RequestHeader("Authorization") String authorization)
    {

        userService.setImage(file, username);

        return ResponseEntity.status(HttpStatus.OK).body("File saved successfully");
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/get_user_by_username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(
            @PathVariable String username
    ) {

        UserDTO userDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok().body(userDTO);


    }




    @CrossOrigin(origins = origin)
    @GetMapping("/get_user_by_token")
    public ResponseEntity<UserDTO> getUserByToken(
            @RequestHeader("Authorization") String authorization
    ) {

        UserDTO userDTO = userService.getUserByToken(authorization);



        return ResponseEntity.ok().body(userDTO);


    }





    @CrossOrigin(origins = origin)
    @GetMapping("/get_communities_of_user/{username}")
    public ResponseEntity<List<CommunityDTO>> getCommunitiesUserIsMemberOf(
            @PathVariable String username
    ) {
        System.out.println("In func");
        List<CommunityDTO> communities = userService.getCommunitiesUserIsMemberOf(username);



        return ResponseEntity.ok().body(communities);


    }



    //sett profilbilde
    //få bruker






}
