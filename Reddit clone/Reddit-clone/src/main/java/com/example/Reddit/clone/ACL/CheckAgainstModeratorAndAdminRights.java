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

    public boolean userIsModerator(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return (userRepository.findCommunitiesUserIsModeratorOf(SecurityUtils.getUsername()) .contains(community));
    }


    public boolean userIsAdministrator(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        return userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() ) .contains(community);
    }


    public boolean userIsAdministratorOrModerator(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        return (userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() ).contains(community)
                || userRepository.findCommunitiesUserIsModeratorOf( SecurityUtils.getUsername() ).contains(community));
    }


    public boolean canChangeCommunityImage(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (!user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeCommunityImage();
         return false;
    }


    public boolean canChangeWallpaper(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeWallpaper();
        return false;
    }


    public boolean canBanUsers(long communityId, String usernameToBan) {
        Community community = communityRepository.findById(communityId)
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
    public boolean userCanSubscribe(long communityId) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        //user is not banned
        if (userRepository.getCommunitiesBannedFrom(user.getUsername()).contains(community))
            return false;
        return true;
    }


    public boolean canDeleteCommunity(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanDeleteCommunity();
        return false;
    }


    public boolean canDeleteOthersComments(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        // if user is admin
        if (communityRepository.findAdminsOfCommunityById(communityId).contains(user))
            return true;
        // if user is a moderator with the right to delete comment
        else if (communityRepository.findModsOfCommunityById(communityId).contains(user))
            return community.getModeratorCanDeleteOthersComments();
        // user is regular user, without the right to delete comments
        return false;
    }


    public boolean canDeleteOthersPosts(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (communityRepository.findAdminsOfCommunityById( communityId ) .contains( user ))
            return true;
        else if (communityRepository.findModsOfCommunityById( communityId ).contains(user))
            return community.getModeratorCanDeleteOthersPosts();
        return false;
    }


    public boolean canMakeAnnouncments(long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
        User user = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanMakeAnnouncement();
        return false;
    }


    public boolean canChangeCommunityDescription(long communityId) {
        Community community = communityRepository.findById(communityId)
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
