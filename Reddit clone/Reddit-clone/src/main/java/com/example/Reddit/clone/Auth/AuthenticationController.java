package com.example.Reddit.clone.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    final String origin = "http://localhost:3000";


    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        System.out.println("inregister");
        return ResponseEntity.ok(service.register(request));
    }



    @CrossOrigin(origins = origin)
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        System.out.println("inauth");
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