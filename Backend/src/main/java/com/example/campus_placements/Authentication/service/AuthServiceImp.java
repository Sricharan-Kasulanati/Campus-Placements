package com.example.campus_placements.Authentication.service;

import com.example.campus_placements.Authentication.dto.AuthResponse;
import com.example.campus_placements.Authentication.dto.LoginRequest;
import com.example.campus_placements.Authentication.dto.SignUpRequest;
import com.example.campus_placements.security.JwtService;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImp implements AuthService{
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final UserDetailsService uds;

    private final JwtService jwt;

    public AuthServiceImp(UserRepository users, PasswordEncoder encoder,
                           UserDetailsService uds, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.uds = uds; this.jwt = jwt;
    }

    @Override
    @Transactional
    public AuthResponse signup(SignUpRequest req) {
        if (users.existsByEmail(req.getEmail())) return null;
        User user = new User(req.getFirstName(), req.getLastName(), req.getEmail(),
                encoder.encode(req.getPassword()), req.getRole());
        users.save(user);
        String token = jwt.generate(user.getEmail(), List.of("ROLE_" + user.getRole().name()));
        return new AuthResponse(token, user.fullName(), user.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        UserDetails ud = uds.loadUserByUsername(req.getEmail());
        if (!encoder.matches(req.getPassword(), ud.getPassword()))
            throw new BadCredentialsException("Invalid credentials");

        String token = jwt.generate(ud.getUsername(),
                ud.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        User u = users.findByEmail(req.getEmail()).orElseThrow();
        return new AuthResponse(token, u.fullName(), u.getRole());
    }
}
