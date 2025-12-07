package com.example.campus_placements.Authentication.service;

import com.example.campus_placements.Authentication.dto.AuthResponse;
import com.example.campus_placements.Authentication.dto.LoginRequest;
import com.example.campus_placements.Authentication.dto.SignUpRequest;
import com.example.campus_placements.security.JwtService;
import com.example.campus_placements.user.model.Role;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImpTest {
    @Mock
    private UserRepository users;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwt;
    @Mock
    private UserDetailsService uds;

    @InjectMocks
    private AuthServiceImp authService;

    @Test
    void signUpTest() {
        SignUpRequest req = new SignUpRequest();
        req.setFirstName("Test");
        req.setLastName("User");
        req.setEmail("test@example.com");
        req.setPassword("Password123");
        req.setRole(Role.STUDENT);

        when(users.existsByEmail("test@example.com")).thenReturn(false);
        when(encoder.encode("Password123")).thenReturn("ENCODED_PASS");
        when(users.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwt.generate(eq("test@example.com"), anyList())).thenReturn("DUMMY_TOKEN");

        AuthResponse res = authService.signup(req);

        assertNotNull(res);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(users).save(userCaptor.capture());
        User saved = userCaptor.getValue();

        assertEquals("test@example.com", saved.getEmail());
        assertEquals("ENCODED_PASS", saved.getPasswordHash());
        assertEquals(Role.STUDENT, saved.getRole());
    }

    @Test
    void loginTest() {
        String email = "student@example.com";
        String rawPassword = "password123";
        String encodedPassword = "ENCODED";

        LoginRequest req = new LoginRequest();
        req.setEmail(email);
        req.setPassword(rawPassword);
        UserDetails securityUser = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(encodedPassword)
                .authorities("ROLE_STUDENT")
                .build();

        when(uds.loadUserByUsername(email)).thenReturn(securityUser);
        when(encoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwt.generate(eq(email), anyList())).thenReturn("DUMMY_TOKEN");

        User dbUser = new User();
        dbUser.setFirstName("Test");
        dbUser.setLastName("User");
        dbUser.setEmail(email);
        dbUser.setPasswordHash(encodedPassword);
        dbUser.setRole(Role.STUDENT);

        when(users.findByEmail(email)).thenReturn(Optional.of(dbUser));

        AuthResponse res = authService.login(req);

        assertNotNull(res);
        assertEquals("DUMMY_TOKEN", res.getToken());
        assertEquals("Test User", res.getFullName());
        assertEquals(Role.STUDENT, res.getRole());
    }

    @Test
    void loginFailedTest() {
        String email = "student@example.com";
        String rawPassword = "wrongPass";

        LoginRequest req = new LoginRequest();
        req.setEmail(email);
        req.setPassword(rawPassword);

        UserDetails securityUser = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("ENCODED")
                .authorities("ROLE_STUDENT")
                .build();

        when(uds.loadUserByUsername(email)).thenReturn(securityUser);
        when(encoder.matches(rawPassword, "ENCODED")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(req));

        verify(jwt, never()).generate(anyString(), anyList());
        verify(users, never()).findByEmail(anyString());
    }
}

