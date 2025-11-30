package com.example.campus_placements.admin.controller;


import com.example.campus_placements.admin.dto.AdminStudentResponse;
import com.example.campus_placements.admin.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminStudentService) {
        this.adminService = adminStudentService;
    }

    @GetMapping
    public List<AdminStudentResponse> listStudents() {
        return adminService.listAllStudents();
    }

    @GetMapping("/{studentId}")
    public AdminStudentResponse getStudent(@PathVariable Long studentId) {
        return adminService.getStudent(studentId);
    }
}
