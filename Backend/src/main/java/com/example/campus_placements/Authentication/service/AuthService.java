package com.example.campus_placements.Authentication.service;

import com.example.campus_placements.Authentication.dto.AuthResponse;
import com.example.campus_placements.Authentication.dto.LoginRequest;
import com.example.campus_placements.Authentication.dto.SignUpRequest;

public interface AuthService {
    AuthResponse signup(SignUpRequest request);
    AuthResponse login(LoginRequest request);
}
