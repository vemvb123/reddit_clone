package com.example.Reddit.clone.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDTO {

    private String pathToProfileImage;
    private String username;
    private String role;
    private Boolean isFriendOfUser;



}
