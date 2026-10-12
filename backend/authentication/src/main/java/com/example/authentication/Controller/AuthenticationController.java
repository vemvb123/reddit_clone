package com.example.authentication.Controller;

import com.example.authentication.DTO.AuthenticationRequest;
import com.example.authentication.DTO.AuthenticationResponse;
import com.example.authentication.DTO.RegisterRequest;
import com.example.authentication.Exception.UsernameAlreadyExists;
import com.example.authentication.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    final String origin = "http://localhost:3000";

    @Autowired
    private AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws UsernameAlreadyExists {
        log.info("Call to register user {}", request.toString());
        return ResponseEntity.ok(service.register(request));
    }


    @CrossOrigin(origins = origin)
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        log.info("Call to authenticate user {}", request.toString());
        return ResponseEntity.ok(service.authenticate(request));

    }

    @CrossOrigin(origins = origin)
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader("Authorization") String authorization)
    {
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/user")
    public ResponseEntity<String> user(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body("ok");

    }


}