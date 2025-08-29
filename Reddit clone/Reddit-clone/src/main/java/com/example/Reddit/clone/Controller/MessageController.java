package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CheckAgainstModeratorAndAdminRights;
import com.example.Reddit.clone.ACL.OwnerCheck;
import com.example.Reddit.clone.DTO.MessageDTO;
import com.example.Reddit.clone.Services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/message")
@EnableAutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MessageController {

    final String origin = "http://localhost:3000";

    @Autowired
    private MessageService messageService;

    @Autowired
    private CheckAgainstModeratorAndAdminRights checkAgainstModeratorAndAdminRights;

    @Autowired
    private OwnerCheck ownerCheck;

    @CrossOrigin(origins = origin)
    @GetMapping("/get10MessageToUser/{page}")
    public ResponseEntity<List<MessageDTO>> get10MessageToUser(
            @PathVariable Integer page
    ) {
        List<MessageDTO> messages = messageService.get10MessagesToUser(page);
        return ResponseEntity.ok().body(messages);
    }




    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityName)"))
    @GetMapping("/getRequestsToJoinCommunity/{page}/{communityName}")
    public ResponseEntity<List<MessageDTO>> get10RequestsToJoinCommunity(
            @PathVariable Integer page,
            @PathVariable String communityName
    ) {
        List<MessageDTO> messages = messageService.getRequestsToJoinCommunity(page, communityName);
        return ResponseEntity.ok().body(messages);
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userCanRequestToJoinCommunity(#communityName)"))
    @PostMapping("/requestToJoinCommunity/{communityName}")
    public ResponseEntity<String> requestToJoinCommunity(
            @PathVariable String communityName
    ) {
         messageService.requestToJoinCommunity(communityName);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully requested to join community");
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityName)"))
    @PostMapping("/acceptRequestToJoinCommunity/{communityName}/{usernameRequestingToJoin}")
    public ResponseEntity<String> requestToJoinCommunity(
            @PathVariable String communityName,
            @PathVariable String usernameRequestingToJoin
    ) {
        messageService.acceptRequestToJoinCommunity(communityName, usernameRequestingToJoin);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully accepted request to join community");
    }



    @CrossOrigin(origins = origin)
    @PreAuthorize(("@ownerCheck.userOwnsMessage(#messageId)"))
    @DeleteMapping("/deleteMessage/{messageId}")
    public ResponseEntity<String> get10MessageToUser(
            @PathVariable Long messageId
    ) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted message");
    }



}
