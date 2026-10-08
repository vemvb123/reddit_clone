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


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityId)"))
    @GetMapping("/join_community/{page}/{communityId}")
    public List<MessageDTO> get10RequestsToJoinCommunity(
            @PathVariable Integer page,
            @PathVariable long communityId
    ) {
        return messageService.getRequestsToJoinCommunity(page, communityId);
    }


    @PreAuthorize(("@ownerCheck.userCanRequestToJoinCommunity(#communityId)"))
    @PostMapping("/join_community/{communityId}")
    public ResponseText requestToJoinCommunity(
            @PathVariable long communityId
    ) {
         return messageService.requestToJoinCommunity(communityId);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityId)"))
    @PostMapping("/accept_join_community/{communityId}/{usernameRequestingToJoin}")
    public ResponseText requestToJoinCommunity(
            @PathVariable long communityId,
            @PathVariable String usernameRequestingToJoin
    ) {
        return messageService.acceptRequestToJoinCommunity(communityId, usernameRequestingToJoin);
    }


    @PreAuthorize(("@ownerCheck.userOwnsMessage(#messageId)"))
    @DeleteMapping("/{messageId}")
    public ResponseText get10MessageToUser(
            @PathVariable Long messageId
    ) {
        return messageService.deleteMessage(messageId);
    }



}
