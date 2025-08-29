package com.example.Reddit.clone.Controller;

import com.example.Reddit.clone.ACL.CheckAgainstModeratorAndAdminRights;
import com.example.Reddit.clone.DTO.*;
import com.example.Reddit.clone.Services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/community")
@EnableAutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CommunityController {




    final String origin = "http://localhost:3000";

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CheckAgainstModeratorAndAdminRights checkAgainstModeratorAndAdminRights;

    //@Autowired
    //private OwnerCheck ownerCheck;

    //@Autowired
    //private CanPartakeInCommunity canPartakeInCommunity;


    @CrossOrigin(origins = origin)
    @GetMapping("/user_has_role_in_community/{communityName}")
    public ResponseEntity<UserHasRoleInCommunityResponse> userHasRoleInCommunity(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization)
    {

        System.out.println("herfrada");
        UserHasRoleInCommunityResponse response = communityService.userHasRoleInCommunity(authorization, communityName);

        return ResponseEntity.ok().body(response);
    }

    @CrossOrigin(origins = origin)
    @PostMapping("/save_community")
    public ResponseEntity<CommunityDTO> saveCommunity(
            @RequestBody CommunityDTO communityDTO,
            @RequestHeader("Authorization") String authorization)
    {


        CommunityDTO DTOfromSavedComunity = communityService.saveCommunity(communityDTO, authorization);

        return ResponseEntity.ok().body(DTOfromSavedComunity);
    }

    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canDeleteCommunity(#authorization, #communityName)"))
    @DeleteMapping("/delete_community/{communityName}")
    public ResponseEntity<String> deleteCommunity(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
    ) {
        System.out.println("Infuncy");
        communityService.deleteCommunity(communityName);

        return ResponseEntity.status(HttpStatus.OK).body("Deleted community");
    }




    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#authorization, #communityName)"))
    @PostMapping("/change_moderator_rights/{communityName}")
    public ResponseEntity<String> changeModeratorRights(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization,
            @RequestBody ModeratorRightsDTO changeModeratorRightsRequest
    ) {
        System.out.println("hallofracontroller");
        communityService.changeModeratorRights(communityName, changeModeratorRightsRequest);

        return ResponseEntity.status(HttpStatus.OK).body("Changed moderator rights");

    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#authorization, #communityName)"))
    @PostMapping("/remove_moderator_rights/{communityName}/{userToHaveModeratorRightsRemoved}")
    public ResponseEntity<String> removeModeratorRightsFromUser(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization,
            @PathVariable String userToHaveModeratorRightsRemoved
    ) {
        communityService.removeModeratorRightsFromUser(communityName, userToHaveModeratorRightsRemoved);

        return ResponseEntity.status(HttpStatus.OK).body("Removed moderator rights");

    }




    @CrossOrigin(origins = origin)
    @GetMapping("/get_members/{communityName}")
    public ResponseEntity<Set<MemberDTO>> getMembers(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
            ) {
        Set<MemberDTO> members = communityService.getMembers(communityName, authorization);

        return ResponseEntity.ok().body(members);
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministratorOrModerator(#authorization, #communityName)"))
    @GetMapping("/get_moderator_rights/{communityName}")
    public ResponseEntity<ModeratorRightsDTO> getModeratorRights(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
    ) {
        System.out.println("herfra1");
        ModeratorRightsDTO moderatorRightsDTO = communityService.getModeratorRights(communityName);

        return ResponseEntity.ok().body(moderatorRightsDTO);
    }





    //Todo: Kan bare bli invitert til private, brukeren selv må klikke for å bli bruker
    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userCanSubscribe(#authorization, #communityName)"))
    @PostMapping("/makeUserBecomeMember/{communityName}")
    public ResponseEntity<String> makeUserBecomeMember(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
    )
    {
        System.out.println("herja?");
        communityService.makeUserBecomeMember(authorization, communityName);


        return ResponseEntity.status(HttpStatus.OK).body("User became member successfully");
    }

    //authorization - The user making the other user become admin
    //userToBecomeAdmin - The user reciving admin rights in community
    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#authorization, #communityName)"))
    @PostMapping("/makeUserAdmin/{communityName}/{usernameToBecomeAdmin}")
    public ResponseEntity<String> makeUserAdmin(
            @PathVariable String communityName,
            @PathVariable String usernameToBecomeAdmin,
            @RequestHeader("Authorization") String authorization
    )
    {
        System.out.println("OKEY");
        communityService.makeUserBecomeAdmin(usernameToBecomeAdmin, communityName);


        return ResponseEntity.status(HttpStatus.OK).body("User became admin successfully");
    }


    //authorization - The user making the other user become mod
    //userToBecomeAdmin - The user reciving mod rights in community
    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#authorization, #communityName)"))
    @PostMapping("/makeUserBecomeMod/{communityName}/{usernameToBecomeMod}")
    public ResponseEntity<String> makeUserBecomeMod(
            @PathVariable String communityName,
            @PathVariable String usernameToBecomeMod,
            @RequestHeader("Authorization") String authorization
    )
    {
        System.out.println("Fader");
        communityService.makeUserBecomeMod(usernameToBecomeMod, communityName);


        return ResponseEntity.status(HttpStatus.OK).body("User became admin successfully");
    }


    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canBanUsers(#authorization, #communityName, #usernameToBan)"))
    @PostMapping("/banUserFromCommunity/{communityName}/{usernameToBan}")
    public ResponseEntity<String> banUserFromCommunity(
            @PathVariable String communityName,
            @PathVariable String usernameToBan,
            @RequestHeader("Authorization") String authorization
    ) {
        System.out.println("huh");
        communityService.banUserFromCommunity(communityName, usernameToBan);


        return ResponseEntity.status(HttpStatus.OK).body("Banned users successfully");

    }




    @CrossOrigin(origins = origin)
    @PostMapping("/unsubscribeFromCommunity/{communityName}")
    public ResponseEntity<String> unsubscribeFromCommunity(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
    ) {

        communityService.unsubscribeUserFromCommunity(authorization, communityName);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully unsubscribed from community");

    }







    //Todo: Bare administrator kan gjøre dette
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canChangeWallpaper(#authorization, #communityName)"))
    @PostMapping("/setWallpaperForCommunity/{communityName}")
    public ResponseEntity<String> setWallpaper(@RequestParam("file") MultipartFile file, @PathVariable String communityName, @RequestHeader("Authorization") String authorization)
    {


        communityService.setWallpaper(file, communityName);

        return ResponseEntity.status(HttpStatus.OK).body("File saved successfully");
    }


    //Todo: Bare administrator kan gjøre dette
    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.canChangeCommunityImage(#authorization, #communityName)"))
    @PostMapping("/setLogoForCommunity/{communityName}")
    public ResponseEntity<String> setLogo(@RequestParam("file") MultipartFile file, @PathVariable String communityName, @RequestHeader("Authorization") String authorization)
    {
        System.out.println("herfrada");
        communityService.setLogo(file, communityName);

        return ResponseEntity.status(HttpStatus.OK).body("File saved successfully");
    }


    @CrossOrigin(origins = origin)
    @GetMapping("/get_community_by_name/{communityName}")
    public ResponseEntity<CommunityDTO> getCommunityByName(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
    ) {

        CommunityDTO communityDTO = communityService.getCommunityByName(communityName);
        return ResponseEntity.ok().body(communityDTO);


    }

    @CrossOrigin(origins = origin)
    @GetMapping("/get_community_by_name_without_token/{communityName}")
    public ResponseEntity<CommunityDTO> getCommunityByName(
            @PathVariable String communityName
    ) {

        CommunityDTO communityDTO = communityService.getCommunityByName(communityName);
        return ResponseEntity.ok().body(communityDTO);


    }

    @CrossOrigin(origins = origin)
    @GetMapping("/getUsers/{communityName}")
    public ResponseEntity<Set<UserDTO>> getUsers(
            @PathVariable String communityName
    ) {
        System.out.println("hallodameg");
        Set<UserDTO> userDTOs = communityService.getUsers(communityName);
        return ResponseEntity.ok().body(userDTOs);


    }




    @CrossOrigin(origins = origin)
    @PreAuthorize(("@checkAgainstModeratorAndAdminRights.userIsAdministrator(#authorization, #communityName)"))
    @PostMapping("/removeMod/{communityName}/{usernameToRemove}")
    public ResponseEntity<String> removeMod(
            @PathVariable String communityName
    ) {




        return ResponseEntity.status(HttpStatus.OK).body("Mod removed successfully");


    }

    @CrossOrigin(origins = origin)
    @PostMapping("/removeAdmin/{communityName}")
    public ResponseEntity<String> removeAdmin(
            @PathVariable String communityName,
            @RequestHeader("Authorization") String authorization
    ) {





        return ResponseEntity.status(HttpStatus.OK).body("Admin removed successfully");


    }



}
