package com.example.campus_placements.Authentication.dto;

import com.example.campus_placements.user.model.Role;

public class AuthResponse {
    private final String token;
    private final String fullName;
    private final Role role;
    public AuthResponse(String token, String fullName, Role role){
        this.token=token; this.fullName=fullName; this.role=role;
    }
    public String getToken(){return token;}
    public String getFullName(){return fullName;}
    public Role getRole(){return role;}
}
