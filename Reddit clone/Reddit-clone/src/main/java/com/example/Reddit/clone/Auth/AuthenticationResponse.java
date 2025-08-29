package com.example.Reddit.clone.Auth;

import com.example.Reddit.clone.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Set<Role> roles = new HashSet<>();
    private String token;

}
