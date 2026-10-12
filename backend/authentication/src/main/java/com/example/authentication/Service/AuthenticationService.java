package com.example.authentication.Service;

import com.example.authentication.DTO.AuthenticationRequest;
import com.example.authentication.DTO.AuthenticationResponse;
import com.example.authentication.DTO.UserCreatedEvent;
import com.example.authentication.Entity.RoleEnum;
import com.example.authentication.Entity.User;
import com.example.authentication.Exception.UserNotFound;
import com.example.authentication.Exception.UsernameAlreadyExists;
import com.example.authentication.DTO.RegisterRequest;
import com.example.authentication.Publisher.UserEventPublisher;
import com.example.authentication.Repository.UserRepository;
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
    private final UserEventPublisher userEventPublisher;

    public AuthenticationResponse register(RegisterRequest request) throws UsernameAlreadyExists {
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
        if (userRepository.existsByUsername(request.username()))
            throw new UsernameAlreadyExists(request.username());

        User savedUser = userRepository.save(user);
        UserCreatedEvent event = new UserCreatedEvent(savedUser.getUsername());
        userEventPublisher.publishUserCreated(event);

        log.info("Saved user {}", savedUser);
        var jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse( jwtToken );

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var user  = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFound(request.username()));
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
