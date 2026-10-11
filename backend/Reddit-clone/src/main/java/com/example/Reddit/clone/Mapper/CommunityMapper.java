package com.example.Reddit.clone.Mapper;

import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.MemberDTO;
import com.example.Reddit.clone.DTO.ModeratorRightsDTO;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapper {

    public Community dtoToEntity(CommunityDTO dto) {
        Community community = new Community();
        community.setTitle(dto.title());
        community.setDescription(dto.description());
        community.setCommunityType(dto.communityType());
        community.setCommunityImage(dto.communityImage());
        return community;
    }


    public CommunityDTO entityToDto(Community entity) {
        return new CommunityDTO(
                entity.getTitle(),
                entity.getDescription(),
                entity.getCommunityType(),
                entity.getCommunityImage(),
                "",
                entity.getCommunityWallpaper(),

                entity.getModeratorCanChangeWallpaper(),
                entity.getModeratorCanChangeCommunityImage(),
                entity.getModeratorCanDeleteOthersPosts(),
                entity.getModeratorCanDeleteOthersComments(),
                entity.getModeratorCanBanUser(),
                entity.getModeratorCanDeleteCommunity(),
                entity.getModeratorCanMakeAnnouncement(),
                entity.getModeratorCanChangeCommunityDescription()
        );
    }


    public ModeratorRightsDTO communityToModeratorRightsDto(Community community) {
        return new ModeratorRightsDTO(
                community.getModeratorCanBanUser(),
                community.getModeratorCanChangeCommunityImage(),
                community.getModeratorCanChangeWallpaper(),
                community.getModeratorCanDeleteCommunity(),
                community.getModeratorCanDeleteOthersComments(),
                community.getModeratorCanDeleteOthersPosts(),
                community.getModeratorCanMakeAnnouncement(),
                community.getModeratorCanChangeCommunityDescription()
        );
    }


    public MemberDTO userToMemberDto(User user, boolean isFriend, String role) {
             return new MemberDTO(
                user.getUsername(),
                user.getPathToProfileImage(),
                role,
                isFriend);
    };








}
