package com.example.campus_placements.user.service;

import com.example.campus_placements.user.dto.UpdateRequest;
import com.example.campus_placements.user.dto.UserResponse;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository users;

    public UserServiceImp(UserRepository users) { this.users = users; }

    @Override
    public UserResponse getCurrentUser() {
        User u = currentUserEntity();
        return toResponse(u);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUser(UpdateRequest req) {
        User u = currentUserEntity();

        if (!u.getEmail().equalsIgnoreCase(req.getEmail()) && users.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        users.save(u);

        return toResponse(u);
    }

    private User currentUserEntity() {
        String email = currentEmail();
        return users.findByEmail(email).orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) throw new IllegalStateException("Not authenticated");
        return auth.getName();
    }

    private static UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getRole());
    }
}
