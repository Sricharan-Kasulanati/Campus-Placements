package com.example.campus_placements.admin.dto;

import java.util.List;

public class AdminAnalyticsOverviewDTO {

    private long totalStudents;
    private long totalCompanies;
    private long totalQuizzes;
    private long totalAttempts;

    private List<AdminCompanyAnalyticsDTO> companies;

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getTotalCompanies() { return totalCompanies; }
    public void setTotalCompanies(long totalCompanies) { this.totalCompanies = totalCompanies; }

    public long getTotalQuizzes() { return totalQuizzes; }
    public void setTotalQuizzes(long totalQuizzes) { this.totalQuizzes = totalQuizzes; }

    public long getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(long totalAttempts) { this.totalAttempts = totalAttempts; }

    public List<AdminCompanyAnalyticsDTO> getCompanies() { return companies; }
    public void setCompanies(List<AdminCompanyAnalyticsDTO> companies) { this.companies = companies; }
}
