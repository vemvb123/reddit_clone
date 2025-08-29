package com.example.Reddit.clone.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHasRoleInCommunityResponse {

    UserHasRoleInCommunityResponseRoles role;


}
