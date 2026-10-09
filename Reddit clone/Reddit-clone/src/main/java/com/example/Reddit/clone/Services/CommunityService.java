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


    public ResponseText setWallpaper(MultipartFile file, long communityId) {
        Community community = communityRepository.findById(communityId)
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
        return new ResponseText(ResponseTextType.FILE_SAVED);
    }


    public CommunityDTO getCommunityByName(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return communityMapper.entityToDto(community);
    }


    public CommunityDTO getCommunityById(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return communityMapper.entityToDto(community);
    }


    public ResponseText setLogo(MultipartFile file, long communityId) {
        Community community = communityRepository.findById(communityId)
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
        return new ResponseText(ResponseTextType.FILE_SAVED);
    }


    public ResponseText makeUserBecomeMember(long communityId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        //check if user is not already a member
        if (userRepository.findCommunitiesUserIsMemberOf(user.getUsername()) .contains(community))
            throw new CommunityException(CommunityError.ALREADY_MEMBER, user.getUsername(), community.getTitle());
        else
            communityRepository.addUserToCommunity(user.getId(), community.getId());
        return new ResponseText(ResponseTextType.BECAME_MEMBER);
    }


    public ResponseText deleteCommunity(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        postRepository.deleteAll( postRepository.findLaterPostAfterPostById( communityId) );
        communityRepository.deleteCommunity(community.getId());
        return new ResponseText(ResponseTextType.DELETED);
    }


    public ResponseText makeUserBecomeAdmin(String usernameToBecomeAdmin, long communityId) {
        User user = userRepository.findByUsername( usernameToBecomeAdmin )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findById( communityId )
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        //checks if user is a member, if user is not already a moderator (if so, remove mod rights and upgrade to admin rights), if user is not already an administrator
        if (! userRepository.findCommunitiesUserIsMemberOf(usernameToBecomeAdmin) .contains(community))
            throw new CommunityException(CommunityError.ALREADY_MEMBER, usernameToBecomeAdmin, community.getTitle());
        if (userRepository.findCommunitiesUserIsAdministratorOf(usernameToBecomeAdmin) .contains(community))
            throw new CommunityException(CommunityError.ALREADY_ADMINISTRATOR, usernameToBecomeAdmin, community.getTitle());
        if (userRepository.findCommunitiesUserIsModeratorOf(usernameToBecomeAdmin) .contains(community))
            communityRepository.removeModeratorPowers(user.getId(), community.getId());

        communityRepository.addAdminToCommunity(user.getId(), community.getId());
        return new ResponseText("User became admin successfully");
    }


    public ResponseText makeUserBecomeMod(String usernameToBecomeMod, long communityId) {
        User user = userRepository.findByUsername(usernameToBecomeMod)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        //checks if user is a member, if user is not already a moderator, if user is not already an administrator
        if (!userRepository.findCommunitiesUserIsMemberOf(usernameToBecomeMod).contains(community))
            throw new CommunityException(CommunityError.NOT_MEMBER, usernameToBecomeMod, community.getTitle());
        if (userRepository.findCommunitiesUserIsModeratorOf(usernameToBecomeMod).contains(community))
            throw new CommunityException(CommunityError.ALREADY_MODERATOR, usernameToBecomeMod, community.getTitle());
        if (userRepository.findCommunitiesUserIsAdministratorOf(usernameToBecomeMod).contains(community))
            throw new CommunityException(CommunityError.ALREADY_ADMINISTRATOR, usernameToBecomeMod, community.getTitle());

        communityRepository.addModToCommunity(user.getId(), community.getId());
        return new ResponseText("User became moderator successfully");
    }


    public Set<UserDTO> getUsers(long communityId) {
        Set<User> users = communityRepository.findMembersOfCommunityById(communityId);
        return users.stream()
                .map(user -> userMapper.entityToDto(user))
                .collect(Collectors.toSet());
    }


    public ResponseText changeModeratorRights(long communityId, ModeratorRightsDTO dto) {

        Community community = communityRepository.findById( communityId )
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        community.setModeratorCanChangeCommunityImage( dto.changeCommunityImage() );
        community.setModeratorCanChangeCommunityDescription( dto.changeCommunityDescription() );
        community.setModeratorCanDeleteCommunity( dto.deleteCommunity() );
        community.setModeratorCanChangeWallpaper( dto.changeWallpaper() );
        community.setModeratorCanBanUser( dto.banUsers() );
        community.setModeratorCanDeleteOthersComments( dto.deleteOthersComments() );
        community.setModeratorCanDeleteOthersPosts( dto.deleteOthersPosts() );
        community.setModeratorCanMakeAnnouncement( dto.makeAnnouncements() );
        communityRepository.save(community);

        return new ResponseText(ResponseTextType.CHANGED_RIGHTS);
    }


    public ModeratorRightsDTO getModeratorRights(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(CommunityError.NOT_FOUND, communityId));
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
            if (communityRepository.findAdminsOfCommunityByTitle(community.getTitle()).size() == 1 && communityRepository.findMembersOfCommunityById(community.getId()).size() > 1)
                //if admin is only admin left, while there are still others user, then throw an error. else if admin is only user left, then unsubscribe
                throw new CommunityException(CommunityError.CANNOT_UNSUBSCRIBE_AS_ADMINISTRATOR, user.getUsername(), community.getTitle());
            else
                communityRepository.removeAdminPowers(user.getId(), community.getId());
        communityRepository.unsubscribeUserFromCommunity(user.getId(), community.getId());

        //if community has no members, delete community
        if (communityRepository.findMembersOfCommunityById(community.getId()).isEmpty())
            communityRepository.deleteById(community.getId());
    }


    public ResponseText unsubscribeUserFromCommunity(long communityId) {
        User user = userRepository.findByUsername(jwtService.extractUsername( SecurityUtils.getUsername() ) )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        //check if user is member of community
        if (!userRepository.findCommunitiesUserIsMemberOf(user.getUsername()).contains(community))
            throw new CommunityException(CommunityError.NOT_MEMBER, user.getUsername(), community.getTitle());
        unSubUser(user, community);

        return new ResponseText("Successfully unsubscribed from community");
    }


    public ResponseText banUserFromCommunity(long communityId, String usernameToBan) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername(usernameToBan)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        communityRepository.banUserFromCommunity(user.getId(), community.getId());

        unSubUser(
                userRepository.findByUsername(usernameToBan).orElseThrow(),
                communityRepository.findById(community.getId())
                        .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY))
        );
        return new ResponseText("Banned users successfully");
    }


    public ResponseText removeModeratorRightsFromUser(String communityName, String userToHaveModeratorRightsRemoved) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername(userToHaveModeratorRightsRemoved)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        //check if user is moderator
        if (!userRepository.findCommunitiesUserIsModeratorOf(userToHaveModeratorRightsRemoved).contains(community))
            throw new CommunityException(CommunityError.NOT_MODERATOR, userToHaveModeratorRightsRemoved, communityName);
        communityRepository.removeModeratorPowers(user.getId(), community.getId());
        return new ResponseText(ResponseTextType.REMOVE_MOD);
    }


    public UserHasRoleInCommunityResponse userHasRoleInCommunity(long communityId) {
        // check if user is member
        Set<Community> communitiesMemberOf = userRepository.findCommunitiesUserIsMemberOf(SecurityUtils.getUsername());
        boolean userIsMember = communitiesMemberOf.stream()
            .anyMatch(community -> communityId == community.getId());
        if (!userIsMember)
            return new UserHasRoleInCommunityResponse(UserHasRoleInCommunityResponseRoles.NOT_MEMBER);
        // check if user is admin
        Set<Community> communitiesAdminOf = userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() );
        boolean userIsAdmin = communitiesAdminOf.stream()
                .anyMatch(community -> communityId == community.getId());
        if (userIsAdmin)
            return new UserHasRoleInCommunityResponse(UserHasRoleInCommunityResponseRoles.ADMIN);
        // check if user is mod
        Set<Community> communitiesModOf = userRepository.findCommunitiesUserIsModeratorOf( SecurityUtils.getUsername() );
        boolean userIsMod = communitiesModOf.stream()
                .anyMatch(community -> communityId == community.getId());
        if (userIsMod)
            return new UserHasRoleInCommunityResponse(UserHasRoleInCommunityResponseRoles.MOD);
        // return user is regular member
        return new UserHasRoleInCommunityResponse(UserHasRoleInCommunityResponseRoles.MEMBER);
    }



    private String getRole(User user, Set<User> admins, Set<User> mods) {
        if (admins.contains(user))
            return "ADMIN";
        if (mods.contains(user))
            return "MOD";
        return "MEMBER";
    }


    public Set<MemberDTO> getMembers(long communityId) {
        User foundUser = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        Set<User> admins = communityRepository.findAdminsOfCommunityById(communityId);
        Set<User> mods = communityRepository.findModsOfCommunityById(communityId);
        Set<User> friends = userRepository.getFriendsOfUser(foundUser.getId());
        Set<User> membersUser = communityRepository.findMembersOfCommunityById(communityId);

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
