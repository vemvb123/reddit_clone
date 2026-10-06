package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.*;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Exception.CommunityError;
import com.example.Reddit.clone.Exception.CommunityException;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Mapper.CommunityMapper;
import com.example.Reddit.clone.Mapper.UserMapper;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommunityService {

    @Value("${images.path}")
    public String pathToSaveImages;

    private CommunityRepository communityRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    private JwtService jwtService;

    private CommunityMapper communityMapper;
    private UserMapper userMapper;


    public CommunityDTO saveCommunity(CommunityDTO communityDTO) {
        // finding username of user who made community
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow(() -> new RuntimeException("User not found"));
        Community community = communityMapper.dtoToEntity(communityDTO);

        Community savedCommunity = communityRepository.save(community);
        //adding the user as a member to the community
        communityRepository.addUserToCommunity(user.getId(), savedCommunity.getId());
        //adding the user as a administrator to the community
        communityRepository.addAdminToCommunity(user.getId(), savedCommunity.getId());

        return communityMapper.entityToDto(savedCommunity);
    }


    public void setWallpaper(MultipartFile file, String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

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
                    community.setCommunityWallpaper(file.getOriginalFilename());
                    communityRepository.save(community);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }


    public CommunityDTO getCommunityByName(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return communityMapper.entityToDto(community);
    }


    public void setLogo(MultipartFile file, String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        if (!file.isEmpty()) {
            try {
                if (!new File(pathToSaveImages).exists()) {
                    new File(pathToSaveImages).mkdir();
                }

                String orgName = file.getOriginalFilename();
                String filePath = pathToSaveImages + orgName;
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


    public void makeUserBecomeMember(String communityName) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        //check if user is not already a member
        if (userRepository.findCommunitiesUserIsMemberOf(user.getUsername()) .contains(community))
            throw new CommunityException(CommunityError.ALREADY_MEMBER, user.getUsername(), community.getTitle());
        else
            communityRepository.addUserToCommunity(user.getId(), community.getId());
    }

    public void deleteCommunity(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        postRepository.deleteAll( postRepository.findLaterPostAfterPost( communityName) );
        communityRepository.deleteCommunity(community.getId());
    }


    public void makeUserBecomeAdmin(String usernameToBecomeAdmin, String communityName) {
        User user = userRepository.findByUsername( usernameToBecomeAdmin )   .orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameToBecomeAdmin));
        Community community = communityRepository.findByTitle( communityName )   .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        //checks if user is a member, if user is not already a moderator (if so, remove mod rights and upgrade to admin rights), if user is not already an administrator
        if (! userRepository.findCommunitiesUserIsMemberOf(usernameToBecomeAdmin) .contains(community))
            throw new CommunityException(CommunityError.ALREADY_MEMBER, usernameToBecomeAdmin, communityName);
        if (userRepository.findCommunitiesUserIsAdministratorOf(usernameToBecomeAdmin) .contains(community))
            throw new CommunityException(CommunityError.ALREADY_ADMINISTRATOR, usernameToBecomeAdmin, communityName);
        if (userRepository.findCommunitiesUserIsModeratorOf(usernameToBecomeAdmin) .contains(community))
            communityRepository.removeModeratorPowers(user.getId(), community.getId());

        communityRepository.addAdminToCommunity(user.getId(), community.getId());
    }


    public void makeUserBecomeMod(String usernameToBecomeMod, String communityName) {
        User user = userRepository.findByUsername(usernameToBecomeMod).orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameToBecomeMod));
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        //checks if user is a member, if user is not already a moderator, if user is not already an administrator
        if (!userRepository.findCommunitiesUserIsMemberOf(usernameToBecomeMod).contains(community))
            throw new CommunityException(CommunityError.NOT_MEMBER, usernameToBecomeMod, communityName);
        if (userRepository.findCommunitiesUserIsModeratorOf(usernameToBecomeMod).contains(community))
            throw new CommunityException(CommunityError.ALREADY_MODERATOR, usernameToBecomeMod, communityName);
        if (userRepository.findCommunitiesUserIsAdministratorOf(usernameToBecomeMod).contains(community))
            throw new CommunityException(CommunityError.ALREADY_ADMINISTRATOR, usernameToBecomeMod, communityName);

        communityRepository.addModToCommunity(user.getId(), community.getId());
    }


    public Set<UserDTO> getUsers(String communityName) {
        Set<User> users = communityRepository.findMembersOfCommunity(communityName);
        return users.stream()
                .map(user -> userMapper.entityToDto(user))
                .collect(Collectors.toSet());
    }


    public void changeModeratorRights(String communityName, ModeratorRightsDTO dto) {

        Community community = communityRepository.findByTitle( communityName )
                .orElseThrow(() -> new CommunityException(CommunityError.NOT_FOUND, communityName));

        community.setModeratorCanChangeCommunityImage( dto.changeCommunityImage() );
        community.setModeratorCanChangeCommunityDescription( dto.changeCommunityDescription() );
        community.setModeratorCanDeleteCommunity( dto.deleteCommunity() );
        community.setModeratorCanChangeWallpaper( dto.changeWallpaper() );
        community.setModeratorCanBanUser( dto.banUsers() );
        community.setModeratorCanDeleteOthersComments( dto.deleteOthersComments() );
        community.setModeratorCanDeleteOthersPosts( dto.deleteOthersPosts() );
        community.setModeratorCanMakeAnnouncement( dto.makeAnnouncements() );

        communityRepository.save(community);
    }


    public ModeratorRightsDTO getModeratorRights(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new CommunityException(CommunityError.NOT_FOUND, communityName));
        return communityMapper.communityToModeratorRightsDto(community);
    }


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
                throw new CommunityException(CommunityError.CANNOT_UNSUBSCRIBE_AS_ADMINISTRATOR, user.getUsername(), community.getTitle());
            else
                communityRepository.removeAdminPowers(user.getId(), community.getId());
        communityRepository.unsubscribeUserFromCommunity(user.getId(), community.getId());

        //if community has no members, delete community
        if (communityRepository.findMembersOfCommunity(community.getTitle()).isEmpty())
            communityRepository.deleteById(community.getId());
    }


    public void unsubscribeUserFromCommunity(String communityName) {
        User user = userRepository.findByUsername(jwtService.extractUsername( SecurityUtils.getUsername() ) ).orElseThrow(() -> ExceptionUtils.noUserWithThatName( SecurityUtils.getUsername() ));
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));

        //check if user is member of community
        if (!userRepository.findCommunitiesUserIsMemberOf(user.getUsername()).contains(community))
            throw new CommunityException(CommunityError.NOT_MEMBER, user.getUsername(), communityName);
        unSubUser(user, community);
    }


    public void banUserFromCommunity(String communityName, String usernameToBan) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new CommunityException(CommunityError.NOT_FOUND, communityName));
        User user = userRepository.findByUsername(usernameToBan)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

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
            throw new CommunityException(CommunityError.NOT_MODERATOR, userToHaveModeratorRightsRemoved, communityName);
        communityRepository.removeModeratorPowers(user.getId(), community.getId());
    }


    public UserHasRoleInCommunityResponse userHasRoleInCommunity(String communityName) {
        UserHasRoleInCommunityResponse responseToReturn = new UserHasRoleInCommunityResponse();

        // check if user is member
        Set<Community> communitiesMemberOf = userRepository.findCommunitiesUserIsMemberOf(SecurityUtils.getUsername());
        boolean userIsMember = communitiesMemberOf.stream()
            .anyMatch(community -> communityName.equals(community.getTitle()));
        if (!userIsMember) {
            responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.NOT_MEMBER);
            return responseToReturn;
        }

        // check if user is admin
        Set<Community> communitiesAdminOf = userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() );
        boolean userIsAdmin = communitiesAdminOf.stream()
                .anyMatch(community -> communityName.equals(community.getTitle()));
        if (userIsAdmin) {
            responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.ADMIN);
            return responseToReturn;
        }

        // check if user is mod
        Set<Community> communitiesModOf = userRepository.findCommunitiesUserIsModeratorOf( SecurityUtils.getUsername() );
        boolean userIsMod = communitiesModOf.stream()
                .anyMatch(community -> communityName.equals(community.getTitle()));
        if (userIsMod) {
            responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.MOD);
            return responseToReturn;
        }

        // return user is regular member
        responseToReturn.setRole(UserHasRoleInCommunityResponseRoles.MEMBER);
        return responseToReturn;
    }



    private String getRole(User user, Set<User> admins, Set<User> mods) {
        if (admins.contains(user))
            return "ADMIN";
        if (mods.contains(user))
            return "MOD";
        return "MEMBER";
    }


    public Set<MemberDTO> getMembers(String communityName) {
        User foundUser = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        Set<User> admins = communityRepository.findAdminsOfCommunity(communityName);
        Set<User> mods = communityRepository.findModsOfCommunity(communityName);
        Set<User> friends = userRepository.getFriendsOfUser(foundUser.getId());
        Set<User> membersUser = communityRepository.findMembersOfCommunity(communityName);

        Set<MemberDTO> members = friends.stream()
                .map(friend -> communityMapper.userToMemberDto(
                        friend,
                        true,
                        getRole(friend, admins, mods)
                )).collect(Collectors.toSet());

        members.addAll(
                admins.stream()
                        .filter(admin -> !friends.contains(admin))
                        .map(admin -> communityMapper.userToMemberDto(admin, false, "ADMIN"))
                    .toList()
        );

        members.addAll(
                mods.stream()
                        .filter(mod -> !friends.contains(mod))
                        .map(mod -> communityMapper.userToMemberDto(mod, false, "MOD"))
                    .toList()
        );

        members.addAll(
                membersUser.stream()
                        .filter(user -> !friends.contains(user))
                        .filter(user -> !mods.contains(user))
                        .filter(user -> !admins.contains(user))
                        .map(user -> communityMapper.userToMemberDto(user, false, "MEMBER"))
                    .toList()
        );

        return members;
    }
}
