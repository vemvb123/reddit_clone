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


    @GetMapping("/role/{communityName}")
    public UserHasRoleInCommunityResponse userHasRoleInCommunity(@PathVariable String communityName)
    {
        return communityService.userHasRoleInCommunity(communityName);
    }

    @PostMapping
    public CommunityDTO saveCommunity(
            @RequestBody CommunityDTO communityDTO
    ) {
        return communityService.saveCommunity(communityDTO);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canDeleteCommunity(#communityName)"))
    @DeleteMapping("/{communityName}")
    public ResponseText deleteCommunity(
            @PathVariable String communityName
    ) {
        return communityService.deleteCommunity(communityName);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityName)"))
    @PostMapping("/mod_rights/{communityName}")
    public ResponseText changeModeratorRights(
            @PathVariable String communityName,
            @RequestBody ModeratorRightsDTO changeModeratorRightsRequest
    ) {
        return communityService.changeModeratorRights(communityName, changeModeratorRightsRequest);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityName)"))
    @PostMapping("/remove_mod/{communityName}/{userToHaveModeratorRightsRemoved}")
    public ResponseText removeModeratorRightsFromUser(
            @PathVariable String communityName,
            @PathVariable String userToHaveModeratorRightsRemoved
    ) {
        return communityService.removeModeratorRightsFromUser(communityName, userToHaveModeratorRightsRemoved);
    }


    @GetMapping("/members/{communityName}")
    public Set<MemberDTO> getMembers(@PathVariable String communityName) {
        return communityService.getMembers(communityName);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#communityName)"))
    @GetMapping("/mod_rights/{communityName}")
    public ModeratorRightsDTO getModeratorRights(@PathVariable String communityName) {
        return communityService.getModeratorRights(communityName);
    }


    //Todo: Kan bare bli invitert til private, brukeren selv må klikke for å bli bruker
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userCanSubscribe(#communityName)"))
    @PostMapping("/member/{communityName}")
    public ResponseText makeUserBecomeMember(@PathVariable String communityName)
    {
        return communityService.makeUserBecomeMember(communityName);
    }

    //authorization - The user making the other user become admin
    //userToBecomeAdmin - The user reciving admin rights in community
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityName)"))
    @PostMapping("/admin/{communityName}/{usernameToBecomeAdmin}")
    public ResponseText makeUserAdmin(
            @PathVariable String communityName,
            @PathVariable String usernameToBecomeAdmin
    ) {
        return communityService.makeUserBecomeAdmin(usernameToBecomeAdmin, communityName);
    }

    //authorization - The user making the other user become mod
    //userToBecomeAdmin - The user reciving mod rights in community
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#communityName)"))
    @PostMapping("/mod/{communityName}/{usernameToBecomeMod}")
    public ResponseText makeUserBecomeMod(
            @PathVariable String communityName,
            @PathVariable String usernameToBecomeMod
    ) {
        return communityService.makeUserBecomeMod(usernameToBecomeMod, communityName);
    }


    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canBanUsers(#communityName, #usernameToBan)"))
    @PostMapping("/ban/{communityName}/{usernameToBan}")
    public ResponseText banUserFromCommunity(
            @PathVariable String communityName,
            @PathVariable String usernameToBan
    ) {
        return communityService.banUserFromCommunity(communityName, usernameToBan);
    }


    @PostMapping("/unsubscribe/{communityName}")
    public ResponseText unsubscribeFromCommunity(
            @PathVariable String communityName
    ) {
        return communityService.unsubscribeUserFromCommunity(communityName);
    }


    //Todo: Bare administrator kan gjøre dette
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canChangeWallpaper(#communityName)"))
    @PostMapping("/wallpaper/{communityName}")
    public ResponseText setWallpaper(
            @RequestParam("file") MultipartFile file,
            @PathVariable String communityName
    ) {
        return communityService.setWallpaper(file, communityName);
    }

    //Todo: Bare administrator kan gjøre dette
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canChangeCommunityImage(#communityName)"))
    @PostMapping("/logo/{communityName}")
    public ResponseText setLogo(
            @RequestParam("file") MultipartFile file,
            @PathVariable String communityName
    ) {
        return communityService.setLogo(file, communityName);
    }


    @GetMapping("/{communityName}")
    public CommunityDTO getCommunityByName(
            @PathVariable String communityName
    ) {
        return communityService.getCommunityByName(communityName);
    }


    @GetMapping("/users/{communityName}")
    public Set<UserDTO> getUsers(
            @PathVariable String communityName
    ) {
        return communityService.getUsers(communityName);
    }

    // TODO: utfyll
    @PostMapping("/remove_admin/{communityName}")
    public ResponseText removeAdmin(
            @PathVariable String communityName
    ) {
        return new ResponseText("endpoint not developed");
    }



}
