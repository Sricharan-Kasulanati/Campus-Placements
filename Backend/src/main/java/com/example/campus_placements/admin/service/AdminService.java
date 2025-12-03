package com.example.campus_placements.admin.service;

import com.example.campus_placements.admin.dto.AdminAnalyticsOverviewDTO;
import com.example.campus_placements.admin.dto.AdminStudentResponse;

import java.util.List;

public interface AdminService {

    List<AdminStudentResponse> listAllStudents();

    AdminStudentResponse getStudent(Long studentId);

    AdminAnalyticsOverviewDTO getOverview();

}
