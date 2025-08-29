package com.example.Reddit.clone.ACL;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.CommentRepository;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.Services.ExceptionUtils;
import com.example.Reddit.clone.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class CheckAgainstModeratorAndAdminRights {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    public boolean userIsModerator(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));
        return (userRepository.findCommunitiesUserIsModeratorOf(SecurityUtils.getUsername()) .contains(community));
    }


    public boolean userIsAdministrator(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));
        boolean userIsAdministratorIfCommunity = userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() ) .contains(community);
        return userIsAdministratorIfCommunity;
    }

    public boolean userIsAdministratorOrModerator(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityName));
        return (userRepository.findCommunitiesUserIsAdministratorOf( SecurityUtils.getUsername() ).contains(community)
                || userRepository.findCommunitiesUserIsModeratorOf( SecurityUtils.getUsername() ).contains(community));

    }

    public boolean canChangeCommunityImage(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (!user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeCommunityImage();
         return false;
    }


    public boolean canChangeWallpaper(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeWallpaper();
        return false;
    }

    public boolean canBanUsers(String communityName, String usernameToBan) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        User userToBan = userRepository.findByUsername(usernameToBan).orElseThrow(() -> ExceptionUtils.noUserWithThatName(usernameToBan));

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



    public boolean userCanSubscribe(String communityName) {
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();
        Community community = communityRepository.findByTitle(communityName).orElseThrow();

        //user is not banned
        if (userRepository.getCommunitiesBannedFrom(user.getUsername()).contains(community))
            return false;
        return true;

        //if community is private/restricted, there must be an invite beforehand
    }



    public boolean canDeleteCommunity(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanDeleteCommunity();
        return false;
    }

    public boolean canDeleteOthersComments(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (communityRepository.findAdminsOfCommunity(communityName).contains(user))
            return true;
        else if (communityRepository.findModsOfCommunity(communityName).contains(user))
            return community.getModeratorCanDeleteOthersComments();
        return false;
    }


    public boolean canDeleteOthersPosts(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (communityRepository.findAdminsOfCommunity( communityName ) .contains( user ))
            return true;
        else if (communityRepository.findModsOfCommunity(communityName).contains(user))
            return community.getModeratorCanDeleteOthersPosts();
        return false;
    }

    public boolean canMakeAnnouncments(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanMakeAnnouncement();
        return false;
    }

    public boolean canChangeCommunityDescription(String communityName) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow();
        User user = userRepository.findByUsername( SecurityUtils.getUsername() ).orElseThrow();

        if (user.getAdministratorOnCommunities().contains(community))
            return true;
        else if (user.getModeratorOnCommunities().contains(community))
            return community.getModeratorCanChangeCommunityDescription();
        return false;
    }








}
