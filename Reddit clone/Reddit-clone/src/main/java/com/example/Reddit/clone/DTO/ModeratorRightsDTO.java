package com.example.Reddit.clone.DTO;


import com.example.Reddit.clone.Entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorRightsDTO {


    private boolean banUsers;
    private boolean changeCommunityImage;
    private boolean changeWallpaper;
    private boolean deleteCommunity;
    private boolean deleteOthersComments;
    private boolean deleteOthersPosts;
    private boolean makeAnnouncements;
    private boolean changeCommunityDescription;


    public ModeratorRightsDTO mapCommunityRightsToDTO(Community community) {
        banUsers = community.getModeratorCanBanUser();
        changeCommunityImage = community.getModeratorCanChangeCommunityImage();
        changeWallpaper = community.getModeratorCanChangeWallpaper();
        deleteCommunity = community.getModeratorCanDeleteCommunity();
        deleteOthersComments = community.getModeratorCanDeleteOthersComments();
        deleteOthersPosts = community.getModeratorCanDeleteOthersPosts();
        makeAnnouncements = community.getModeratorCanMakeAnnouncement();
        changeCommunityDescription = community.getModeratorCanChangeCommunityDescription();

        return this;
    }










}
