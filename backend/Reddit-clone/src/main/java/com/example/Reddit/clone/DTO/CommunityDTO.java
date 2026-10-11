package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.CommunityType;
import lombok.Builder;

@Builder
public record CommunityDTO(
     String title,
     String description,
     CommunityType communityType,
     String communityImage,
     String username,
     String communityWallpaper,

    // rights of moderators
     Boolean moderatorCanChangeWallpaper,
     Boolean moderatorCanChangeCommunityImage,
     Boolean moderatorCanDeleteOthersPosts,
     Boolean moderatorCanDeleteOthersComments,
     Boolean moderatorCanBanUser,
     Boolean moderatorCanDeleteCommunity,
     Boolean moderatorCanMakeAnnouncement,
     Boolean moderatorCanChangeCommunityDescription
) {}