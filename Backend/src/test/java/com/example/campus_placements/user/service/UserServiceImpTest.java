package com.example.campus_placements.user.service;

import com.example.campus_placements.user.dto.UpdateRequest;
import com.example.campus_placements.user.dto.UserResponse;
import com.example.campus_placements.user.model.Role;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository users;

    @InjectMocks
    private UserServiceImp userService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth =
                new UsernamePasswordAuthenticationToken(email, null, null);
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void updateCurrentUserTest() {
        String oldEmail = "old@example.com";
        authenticateAs(oldEmail);
        User existing = new User();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setEmail(oldEmail);
        existing.setPasswordHash("hash");
        existing.setRole(Role.STUDENT);

        when(users.findByEmail(oldEmail)).thenReturn(Optional.of(existing));

        UpdateRequest req = new UpdateRequest();
        req.setFirstName("NewFirst");
        req.setLastName("NewLast");
        req.setEmail("new@example.com");

        when(users.existsByEmail("new@example.com")).thenReturn(false);

        UserResponse res = userService.updateCurrentUser(req);

        assertNotNull(res);
        assertEquals(1L, res.getId());
        assertEquals("NewFirst", res.getFirstName());
        assertEquals("NewLast", res.getLastName());
        assertEquals("new@example.com", res.getEmail());
        assertEquals(Role.STUDENT, res.getRole());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(users).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("NewFirst", saved.getFirstName());
        assertEquals("NewLast", saved.getLastName());
        assertEquals("new@example.com", saved.getEmail());
    }

    @Test
    void updateUserEmailAlreadyExistsTest() {
        String oldEmail = "old@example.com";
        authenticateAs(oldEmail);

        User existing = new User();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setEmail(oldEmail);
        existing.setRole(Role.STUDENT);

        when(users.findByEmail(oldEmail)).thenReturn(Optional.of(existing));

        UpdateRequest req = new UpdateRequest();
        req.setFirstName("NewFirst");
        req.setLastName("NewLast");
        req.setEmail("taken@example.com");

        when(users.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateCurrentUser(req));

        verify(users, never()).save(any());
    }
}

