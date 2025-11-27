package com.example.campus_placements.Authentication.controller;

import com.example.campus_placements.Authentication.dto.AuthResponse;
import com.example.campus_placements.Authentication.dto.LoginRequest;
import com.example.campus_placements.Authentication.dto.SignUpRequest;
import com.example.campus_placements.Authentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;
    public AuthController(AuthService auth){ this.auth = auth; }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest req){
        AuthResponse response = auth.signup(req);
        if(response == null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Email not found");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
        return ResponseEntity.ok(auth.login(req));
    }
}
