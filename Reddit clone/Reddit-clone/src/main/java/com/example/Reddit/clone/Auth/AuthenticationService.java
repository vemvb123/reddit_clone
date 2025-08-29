package com.example.Reddit.clone.Auth;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.Role;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.RoleRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;

    private final RoleRepository rolerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = rolerRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName((request.getLastname()))
                .email((request.getEmail()))
                .username((request.getUsername()))
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .otherUsersCanSeePosts(true)
                .otherUsersCanSeeComments(true)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        System.out.println("b");
        var user  = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        System.out.println("c");

        var jwtToken = jwtService.generateToken(user);


        return AuthenticationResponse.builder()
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles())
                .token(jwtToken)
                .build();
    }








}
