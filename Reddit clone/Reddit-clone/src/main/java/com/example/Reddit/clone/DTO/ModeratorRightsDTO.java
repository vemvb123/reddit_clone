package com.example.Reddit.clone.DTO;


public record ModeratorRightsDTO(
     boolean banUsers,
     boolean changeCommunityImage,
     boolean changeWallpaper,
     boolean deleteCommunity,
     boolean deleteOthersComments,
     boolean deleteOthersPosts,
     boolean makeAnnouncements,
     boolean changeCommunityDescription
) {}