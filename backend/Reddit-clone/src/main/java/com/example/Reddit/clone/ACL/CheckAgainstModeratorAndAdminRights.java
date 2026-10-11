package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CheckAgainstModeratorAndAdminRights {

    private JwtService jwtService;

    private UserRepository userRepository;
    private CommunityRepository communityRepository;

    public boolean userIsModerator(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return (userRepository.findCommunitiesUserIsModeratorOf(SecurityUtils.getUsername()) .contains(community));
    }


    public boolean userIsAdministrator(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() ) .contains(community);
    }


    public boolean userIsAdministratorOrModerator(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        return (userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() ).contains(community)
                || userRepository.findCommunitiesUserIsModeratorOf( SecurityUtils.getUsername() ).contains(community));
    }


    public boolean canChangeCommunityImage(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (!user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeCommunityImage();
         return false;
    }


    public boolean canChangeWallpaper(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeWallpaper();
        return false;
    }


    public boolean canBanUsers(String communityName, String usernameToBan) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        User userToBan = userRepository.findByUsername(usernameToBan)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user == userToBan)
            return false;
        if (userToBan.getAdministratorOnCommunities().contains(community))
            return false;
        else if (user.getModeratorOnCommunities().contains(community) && userToBan.getModeratorOnCommunities().contains(community))
            return false;

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanBanUser();
        return false;
    }


    // TODO: if community is private/restricted, there must be an invite beforehand
    public boolean userCanSubscribe(String communityName) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        //user is not banned
        if (userRepository.getCommunitiesBannedFrom(user.getUsername()).contains(community))
            return false;
        return true;
    }


    public boolean canDeleteCommunity(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanDeleteCommunity();
        return false;
    }


    public boolean canDeleteOthersComments(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        // if user is admin
        if (communityRepository.findAdminsOfCommunityByTitle(communityName).contains(user))
            return true;
        // if user is a moderator with the right to delete comment
        else if (communityRepository.findModsOfCommunityByTitle(communityName).contains(user))
            return community.getModeratorCanDeleteOthersComments();
        // user is regular user, without the right to delete comments
        return false;
    }


    public boolean canDeleteOthersPosts(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (communityRepository.findAdminsOfCommunityByTitle( communityName ) .contains( user ))
            return true;
        else if (communityRepository.findModsOfCommunityByTitle( communityName ).contains(user))
            return community.getModeratorCanDeleteOthersPosts();
        return false;
    }


    public boolean canMakeAnnouncments(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanMakeAnnouncement();
        return false;
    }


    public boolean canChangeCommunityDescription(String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeCommunityDescription();
        return false;
    }

}
