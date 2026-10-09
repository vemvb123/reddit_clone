package com.example.Reddit.clone.Auth;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.RoleEnum;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.USER);

        User user = new User(
                request.firstname(),
                request.lastname(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.username(),
                roles
        );

        User savedUser = userRepository.save(user);
        log.info("Saved user {}", savedUser);
        var jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(
                jwtToken
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user  = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles(),
                jwtToken
        );

    }


}
