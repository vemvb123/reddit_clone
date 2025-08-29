package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.*;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CommunityService {



    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtService jwtService;


    @Transactional
    public CommunityDTO saveCommunity(CommunityDTO communityDTO, String token) {

        // finding username of user who made community
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Community community = new Community();
        community.setTitle(communityDTO.getTitle());
        community.setDescription(communityDTO.getDescription());
        community.setCommunityType(communityDTO.getCommunityType());
        community.setCommunityImage(communityDTO.getCommunityImage());

        community.setModeratorCanMakeAnnouncement(false);
        community.setModeratorCanDeleteCommunity(false);
        community.setModeratorCanDeleteOthersPosts(false);
        community.setModeratorCanDeleteOthersComments(false);
        community.setModeratorCanBanUser(false);
        community.setModeratorCanChangeWallpaper(false);
        community.setModeratorCanChangeCommunityDescription(false);
        community.setModeratorCanChangeCommunityImage(false);


        Community savedCommunity = communityRepository.save(community);

        //adding the user as a member to the community
        communityRepository.addUserToCommunity(user.getId(), savedCommunity.getId());

        //adding the user as a administrator to the community
        communityRepository.addAdminToCommunity(user.getId(), savedCommunity.getId());

        // returning DTO version of the saved community
        return new CommunityDTO().mapObjectToDTO(savedCommunity);
    }




    public void setWallpaper(MultipartFile file, String communityName) {

        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> new RuntimeException("Community not found"));

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
                    community.setCommunityWallpaper(file.getOriginalFilename());
                    communityRepository.save(community);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }





    }

    public CommunityDTO getCommunityByName(String communityName) {

        Community community = communityRepository.findByTitle(communityName) .orElseThrow(() -> new RuntimeException("Community not found"));
        return new CommunityDTO().mapObjectToDTO(community);
    }



    public void setLogo(MultipartFile file, String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> new RuntimeException("Community not found"));

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
                    community.setCommunityImage(file.getOriginalFilename());
                    communityRepository.save(community);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void makeUserBecomeMember(String authorization, String communityName) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow();

        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> new RuntimeException("Community not found"));

        //check if user is not already a member
        if (userRepository.findCommunitiesUserIsMemberOf(user.getUsername()) .contains(community))
            throw new RuntimeException("User is already a member of the community");
        else
            communityRepository.addUserToCommunity(user.getId(), community.getId());
    }

    @Transactional
    public void deleteCommunity(String communityName) {
        System.out.println("guh1");
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        System.out.println("guh2");
        postRepository.deleteAll( postRepository.findLaterPostAfterPost(communityName) );
        System.out.println("guhhuh");
        communityRepository.deleteCommunity(community.getId());
        System.out.println("guh3");
    }


    @Transactional
    public void makeUserBecomeAdmin(String usernameToBecomeAdmin, String communityName) {
        User user = userRepository.findByUsername( usernameToBecomeAdmin )   .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameToBecomeAdmin));
        Community community = communityRepository.findByTitle( communityName )   .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        System.out.println("HERDA");

        //checks if user is a member, if user is not already a moderator (if so, remove mod rights and upgrade to admin rights), if user is not already an administrator
        if (! userRepository.findCommunitiesUserIsMemberOf(usernameToBecomeAdmin) .contains(community))
            ExceptionUtils.userIsNotAMemberOfCommunity(usernameToBecomeAdmin, communityName);
        if (userRepository.findCommunitiesUserIsAdministratorOf(usernameToBecomeAdmin) .contains(community))
            ExceptionUtils.userIsAlreadyAdministratorOfCommunity(usernameToBecomeAdmin, communityName);
        if (userRepository.findCommunitiesUserIsModeratorOf(usernameToBecomeAdmin) .contains(community))
            communityRepository.removeModeratorPowers(user.getId(), community.getId());

        System.out.println("HERSA");

        communityRepository.addAdminToCommunity(user.getId(), community.getId());

        System.out.println("DERJA");


    }


    @Transactional
    public void makeUserBecomeMod(String usernameToBecomeMod, String communityName) {
        System.out.println("her1");

        User user = userRepository.findByUsername(usernameToBecomeMod).orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameToBecomeMod));
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        //checks if user is a member, if user is not already a moderator, if user is not already an administrator
        if (!userRepository.findCommunitiesUserIsMemberOf(usernameToBecomeMod).contains(community))
            ExceptionUtils.userIsNotAMemberOfCommunity(usernameToBecomeMod, communityName);
        if (userRepository.findCommunitiesUserIsModeratorOf(usernameToBecomeMod).contains(community))
            ExceptionUtils.userIsAlreadyModeratorOfCommunity(usernameToBecomeMod, communityName);
        if (userRepository.findCommunitiesUserIsAdministratorOf(usernameToBecomeMod).contains(community))
            ExceptionUtils.userIsAlreadyAdministratorOfCommunity(usernameToBecomeMod, communityName);


        System.out.println("her2");

        communityRepository.addModToCommunity(user.getId(), community.getId());
        System.out.println("her3");
    }

    public Set<UserDTO> getUsers(String communityName) {

        Set<User> users = communityRepository.findMembersOfCommunity(communityName);

        Set<UserDTO> userDTOs = new HashSet<>();
        for (User user : users)
            userDTOs.add(new UserDTO().mapObjectToDTO(user));
        return userDTOs;


    }

    public void changeModeratorRights(String communityName, ModeratorRightsDTO changeModeratorRightsRequest) {

        Community community = communityRepository.findByTitle( communityName ).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        community.setModeratorCanChangeCommunityImage( changeModeratorRightsRequest.isChangeCommunityImage() );
        community.setModeratorCanChangeCommunityDescription( changeModeratorRightsRequest.isChangeCommunityDescription() );
        community.setModeratorCanDeleteCommunity( changeModeratorRightsRequest.isDeleteCommunity() );
        community.setModeratorCanChangeWallpaper( changeModeratorRightsRequest.isChangeWallpaper() );
        community.setModeratorCanBanUser( changeModeratorRightsRequest.isBanUsers() );
        community.setModeratorCanDeleteOthersComments( changeModeratorRightsRequest.isDeleteOthersComments() );
        community.setModeratorCanDeleteOthersPosts( changeModeratorRightsRequest.isDeleteOthersPosts() );
        community.setModeratorCanMakeAnnouncement( changeModeratorRightsRequest.isMakeAnnouncements() );

        communityRepository.save(community);
    }




    public ModeratorRightsDTO getModeratorRights(String communityName) {
        Community community = communityRepository.findByTitle(communityName) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));
        return new ModeratorRightsDTO().mapCommunityRightsToDTO(community);
    }

    @Transactional
    private void unSubUser(User user, Community community) {

        boolean userIsModerator = false;
        boolean userIsAdmin = false;
        if (userRepository.findCommunitiesUserIsAdministratorOf(user.getUsername()).contains(community))
            userIsAdmin = true;
        else if (userRepository.findCommunitiesUserIsModeratorOf(user.getUsername()).contains(community))
            userIsModerator = true;


        if (userIsModerator)
            communityRepository.removeModeratorPowers(user.getId(), community.getId());
        else if (userIsAdmin)
            if (communityRepository.findAdminsOfCommunity(community.getTitle()).size() == 1 && communityRepository.findMembersOfCommunity(community.getTitle()).size() > 1)
                //if admin is only admin left, while there are still others user, then throw an error. else if admin is only user left, then unsubscribe
                throw new RuntimeException("Cant unsubscribe user " + user.getUsername() + " as admin, since the user is the only admin left, while there are also still other users in the community " + community.getTitle());
            else
                communityRepository.removeAdminPowers(user.getId(), community.getId());
        communityRepository.unsubscribeUserFromCommunity(user.getId(), community.getId());


        //if community has no members, delete community
        if (communityRepository.findMembersOfCommunity(community.getTitle()).size() == 0)
            communityRepository.deleteById(community.getId());
    }

    @Transactional
    public void unsubscribeUserFromCommunity(String authorization, String communityName) {
        System.out.println("hertestesdet");
        User user = userRepository.findByUsername(jwtService.extractUsername(authorization) ).orElseThrow(() -> ExceptionUtils.noUserWithThatName(jwtService.extractUsername(authorization)));
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        //check if user is member of community
        if (!userRepository.findCommunitiesUserIsMemberOf(user.getUsername()).contains(community))
            throw new RuntimeException("User " + user.getUsername() + " must first be member of community " + community.getTitle() + ", before the user can unsubscribe from it");

        unSubUser(user, community);
    }



    @Transactional
    public void banUserFromCommunity(String communityName, String usernameToBan) {
        System.out.println("herdakoman");
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername(usernameToBan).orElseThrow();


        communityRepository.banUserFromCommunity(user.getId(), community.getId());

        unSubUser(
                userRepository.findByUsername(usernameToBan).orElseThrow(),
                communityRepository.findByTitle(communityName).orElseThrow()
        );



    }

    public void removeModeratorRightsFromUser(String communityName, String userToHaveModeratorRightsRemoved) {
        Community community = communityRepository.findByTitle(communityName) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));
        User user = userRepository.findByUsername(userToHaveModeratorRightsRemoved) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(userToHaveModeratorRightsRemoved));

        //check if user is moderator
        if (!userRepository.findCommunitiesUserIsModeratorOf(userToHaveModeratorRightsRemoved).contains(community))
            throw new RuntimeException("Can only remove moderator powers from a user that is a moderator. User " + userToHaveModeratorRightsRemoved + " is not moderator of community " + communityName);

        communityRepository.removeModeratorPowers(user.getId(), community.getId());



    }

    public UserHasRoleInCommunityResponse userHasRoleInCommunity(String authorization, String communityName) {
        String username = jwtService.extractUsername(authorization);

        UserHasRoleInCommunityResponse responseToReturn = new UserHasRoleInCommunityResponse();

        // check if user is member
        Set<Community> communitiesMemberOf = userRepository.findCommunitiesUserIsMemberOf(username);
        boolean userIsMember = false;
        for (Community community : communitiesMemberOf) {
            if (Objects.equals(community.getTitle(), communityName)) {
                userIsMember = true;
                break;
            }
        }
        System.out.println(userIsMember);
        if (!userIsMember) {
            responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.NOT_MEMBER);
            return responseToReturn;
        }

        // check if user is admin
        Set<Community> communitiesAdminOf = userRepository.findCommunitiesUserIsAdministratorOf(username);
        for (Community community : communitiesAdminOf) {
            if (Objects.equals(community.getTitle(), communityName)) {
                responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.ADMIN);
                return responseToReturn;
            }
        }

        // check if user is mod
        Set<Community> communitiesModOf = userRepository.findCommunitiesUserIsModeratorOf(username);
        for (Community community : communitiesModOf)
            if (Objects.equals(community.getTitle(), communityName)) {
                responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.MOD);
                return responseToReturn;
            }

        // return that user is regular member
        responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.MEMBER);
        return responseToReturn;




    }


    public Set<MemberDTO> getMembers(String communityName, String authorization) {

        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));


        Set<MemberDTO> members = new HashSet<>();

        Set<User> admins = communityRepository.findAdminsOfCommunity(communityName);
        Set<User> mods = communityRepository.findModsOfCommunity(communityName);
        Set<User> friends = userRepository.getFriendsOfUser(user.getId());
        Set<User> membersUser = communityRepository.findMembersOfCommunity(communityName);

        for (User friend : friends) {
            String role;
            if (admins.contains(friend))
                role = "ADMIN";
            else if (mods.contains(friend))
                role = "MOD";
            else
                role = "MEMBER";

            var member = MemberDTO.builder()
                    .pathToProfileImage(friend.getPathToProfileImage())
                    .username(friend.getUsername())
                    .isFriendOfUser(true)
                    .role(role)
                    .build();
            members.add(member);

        }

        for (User admin : admins) {
            if (!friends.contains(admin)) {
                var member = MemberDTO.builder()
                        .isFriendOfUser(false)
                        .username(admin.getUsername())
                        .pathToProfileImage(admin.getPathToProfileImage())
                        .role("ADMIN")
                        .build();
                members.add(member);
            }
        }

        for (User mod : mods) {
            if (!friends.contains(mod)) {
                var member = MemberDTO.builder()
                        .isFriendOfUser(false)
                        .username(mod.getUsername())
                        .pathToProfileImage(mod.getPathToProfileImage())
                        .role("MOD")
                        .build();
                members.add(member);
            }
        }

        for (User memberUser : membersUser) {
            if (!friends.contains(memberUser) && !mods.contains(memberUser) && !admins.contains(memberUser)) {

                var member = MemberDTO.builder()
                        .username(memberUser.getUsername())
                        .isFriendOfUser(false)
                        .pathToProfileImage(memberUser.getPathToProfileImage())
                        .role("MEMBER")
                        .build();
                members.add(member);

            }
        }

        return members;





    }
}
