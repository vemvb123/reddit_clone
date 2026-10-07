package com.example.Reddit.clone.Controller;


import com.example.Reddit.clone.ACL.CheckAgainstModeratorAndAdminRights;
import com.example.Reddit.clone.ACL.OwnerCheck;
import com.example.Reddit.clone.DTO.MessageDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.Services.MessageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@EnableAutoConfiguration
@RequiredArgsConstructor
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MessageController {

    private MessageService messageService;
    private CheckAgainstModeratorAndAdminRights checkAgainstModeratorAndAdminRights;
    private OwnerCheck ownerCheck;


    @GetMapping("/user/{page}")
    public List<MessageDTO> get10MessageToUser(
            @PathVariable Integer page
    ) {
        return messageService.get10MessagesToUser(page);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityName)"))
    @GetMapping("/join_community/{page}/{communityName}")
    public List<MessageDTO> get10RequestsToJoinCommunity(
            @PathVariable Integer page,
            @PathVariable String communityName
    ) {
        return messageService.getRequestsToJoinCommunity(page, communityName);
    }


    @PreAuthorize(("@ownerCheck.userCanRequestToJoinCommunity(#communityName)"))
    @PostMapping("/join_community/{communityName}")
    public ResponseText requestToJoinCommunity(
            @PathVariable String communityName
    ) {
         return messageService.requestToJoinCommunity(communityName);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityName)"))
    @PostMapping("/accept_join_community/{communityName}/{usernameRequestingToJoin}")
    public ResponseText requestToJoinCommunity(
            @PathVariable String communityName,
            @PathVariable String usernameRequestingToJoin
    ) {
        return messageService.acceptRequestToJoinCommunity(communityName, usernameRequestingToJoin);
    }


    @PreAuthorize(("@ownerCheck.userOwnsMessage(#messageId)"))
    @DeleteMapping("/{messageId}")
    public ResponseText get10MessageToUser(
            @PathVariable Long messageId
    ) {
        return messageService.deleteMessage(messageId);
    }



}
