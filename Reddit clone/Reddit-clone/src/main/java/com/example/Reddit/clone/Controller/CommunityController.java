package com.example.Reddit.clone.Controller;

import com.example.Reddit.clone.ACL.CheckAgainstModeratorAndAdminRights;
import com.example.Reddit.clone.DTO.*;
import com.example.Reddit.clone.Services.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/community")
@EnableAutoConfiguration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CommunityController {

    private CommunityService communityService;

    private CheckAgainstModeratorAndAdminRights checkAgainstModeratorAndAdminRights;


    @GetMapping("/role/{communityId}")
    public UserHasRoleInCommunityResponse userHasRoleInCommunity(@PathVariable long communityId)
    {
        return communityService.userHasRoleInCommunity(communityId);
    }

    @PostMapping
    public CommunityDTO saveCommunity(
            @RequestBody CommunityDTO communityDTO
    ) {
        return communityService.saveCommunity(communityDTO);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canDeleteCommunity(#communityId)"))
    @DeleteMapping("/{communityId}")
    public ResponseText deleteCommunity(
            @PathVariable long communityId
    ) {
        return communityService.deleteCommunity(communityId);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityId)"))
    @PostMapping("/mod_rights/{communityId}")
    public ResponseText changeModeratorRights(
            @PathVariable long communityId,
            @RequestBody ModeratorRightsDTO changeModeratorRightsRequest
    ) {
        return communityService.changeModeratorRights(communityId, changeModeratorRightsRequest);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityId)"))
    @PostMapping("/remove_mod/{communityId}/{userToHaveModeratorRightsRemoved}")
    public ResponseText removeModeratorRightsFromUser(
            @PathVariable String communityId,
            @PathVariable String userToHaveModeratorRightsRemoved
    ) {
        return communityService.removeModeratorRightsFromUser(communityId, userToHaveModeratorRightsRemoved);
    }


    @GetMapping("/members/{communityId}")
    public Set<MemberDTO> getMembers(@PathVariable long communityId) {
        return communityService.getMembers(communityId);
    }



    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityId)"))
    @GetMapping("/mod_rights/{communityId}")
    public ModeratorRightsDTO getModeratorRights(@PathVariable long communityId) {
        return communityService.getModeratorRights(communityId);
    }


    //Todo: Kan bare bli invitert til private, brukeren selv må klikke for å bli bruker
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userCanSubscribe(#communityId)"))
    @PostMapping("/member/{communityId}")
    public ResponseText makeUserBecomeMember(@PathVariable long communityId)
    {
        return communityService.makeUserBecomeMember(communityId);
    }

    //authorization - The user making the other user become admin
    //userToBecomeAdmin - The user reciving admin rights in community
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityId)"))
    @PostMapping("/admin/{communityId}/{usernameToBecomeAdmin}")
    public ResponseText makeUserAdmin(
            @PathVariable long communityId,
            @PathVariable String usernameToBecomeAdmin
    ) {
        return communityService.makeUserBecomeAdmin(usernameToBecomeAdmin, communityId);
    }

    //authorization - The user making the other user become mod
    //userToBecomeAdmin - The user reciving mod rights in community
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityId)"))
    @PostMapping("/mod/{communityId}/{usernameToBecomeMod}")
    public ResponseText makeUserBecomeMod(
            @PathVariable long communityId,
            @PathVariable String usernameToBecomeMod
    ) {
        return communityService.makeUserBecomeMod(usernameToBecomeMod, communityId);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canBanUsers(#communityId, #usernameToBan)"))
    @PostMapping("/ban/{communityId}/{usernameToBan}")
    public ResponseText banUserFromCommunity(
            @PathVariable long communityId,
            @PathVariable String usernameToBan
    ) {
        return communityService.banUserFromCommunity(communityId, usernameToBan);
    }


    @PostMapping("/unsubscribe/{communityId}")
    public ResponseText unsubscribeFromCommunity(
            @PathVariable long communityId
    ) {
        return communityService.unsubscribeUserFromCommunity(communityId);
    }


    //Todo: Bare administrator kan gjøre dette
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canChangeWallpaper(#communityId)"))
    @PostMapping("/wallpaper/{communityId}")
    public ResponseText setWallpaper(
            @RequestParam("file") MultipartFile file,
            @PathVariable long communityId
    ) {
        return communityService.setWallpaper(file, communityId);
    }

    //Todo: Bare administrator kan gjøre dette
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canChangeCommunityImage(#communityId)"))
    @PostMapping("/logo/{communityId}")
    public ResponseText setLogo(
            @RequestParam("file") MultipartFile file,
            @PathVariable long communityId
    ) {
        return communityService.setLogo(file, communityId);
    }


    @GetMapping("/{communityId}")
    public CommunityDTO getCommunityByName(
            @PathVariable long communityId
    ) {
        return communityService.getCommunityById(communityId);
    }


    @GetMapping("/users/{communityId}")
    public Set<UserDTO> getUsers(
            @PathVariable long communityId
    ) {
        return communityService.getUsers(communityId);
    }

    // TODO: utfyll
    @PostMapping("/remove_admin/{communityId}")
    public ResponseText removeAdmin(
            @PathVariable long communityId
    ) {
        return new ResponseText("endpoint not developed");
    }



}
