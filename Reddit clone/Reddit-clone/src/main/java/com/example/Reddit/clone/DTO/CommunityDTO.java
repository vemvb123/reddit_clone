package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.CommunityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {

    private String title;

    private String description;

    private CommunityType communityType;


    private String communityImage;

    private String username;

    private String communityWallpaper;

    // rights of moderators
    private Boolean moderatorCanChangeWallpaper;
    private Boolean moderatorCanChangeCommunityImage;
    private Boolean moderatorCanDeleteOthersPosts;
    private Boolean moderatorCanDeleteOthersComments;
    private Boolean moderatorCanBanUser;
    private Boolean moderatorCanDeleteCommunity;
    private Boolean moderatorCanMakeAnnouncement;
    private Boolean moderatorCanChangeCommunityDescription;



    public CommunityDTO mapObjectToDTO(Community community) {
        this.title = community.getTitle();
        this.description = community.getDescription();
        this.communityType = community.getCommunityType();
        this.communityImage = community.getCommunityImage();
        this.communityWallpaper = community.getCommunityWallpaper();

        this.moderatorCanChangeWallpaper = community.getModeratorCanChangeWallpaper();
        this.moderatorCanChangeCommunityImage = community.getModeratorCanChangeCommunityImage();
        this.moderatorCanDeleteOthersPosts = community.getModeratorCanDeleteOthersPosts();
        this.moderatorCanDeleteOthersComments = community.getModeratorCanDeleteOthersComments();
        this.moderatorCanBanUser = community.getModeratorCanBanUser();
        this.moderatorCanDeleteCommunity = community.getModeratorCanDeleteCommunity();
        this.moderatorCanMakeAnnouncement = community.getModeratorCanMakeAnnouncement();
        this.moderatorCanChangeCommunityDescription = community.getModeratorCanChangeCommunityDescription();

        return this;
    }

}
