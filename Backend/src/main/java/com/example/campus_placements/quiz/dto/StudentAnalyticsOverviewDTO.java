package com.example.campus_placements.quiz.dto;

import java.util.List;

public class StudentAnalyticsOverviewDTO {
    private List<CompanyLevelAnalyticsDTO> companies;

    public StudentAnalyticsOverviewDTO() {}

    public StudentAnalyticsOverviewDTO(List<CompanyLevelAnalyticsDTO> companies) {
        this.companies = companies;
    }

    public List<CompanyLevelAnalyticsDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyLevelAnalyticsDTO> companies) {
        this.companies = companies;
    }
}

