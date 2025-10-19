package com.example.campus_placements.user.service;

import com.example.campus_placements.user.dto.UpdateRequest;
import com.example.campus_placements.user.dto.UserResponse;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse updateCurrentUser(UpdateRequest req);
}
