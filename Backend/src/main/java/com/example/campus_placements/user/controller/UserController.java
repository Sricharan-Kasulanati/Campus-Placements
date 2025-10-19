package com.example.campus_placements.user.controller;


import com.example.campus_placements.user.dto.UpdateRequest;
import com.example.campus_placements.user.dto.UserResponse;
import com.example.campus_placements.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateRequest req) {
        return ResponseEntity.ok(userService.updateCurrentUser(req));
    }
}
