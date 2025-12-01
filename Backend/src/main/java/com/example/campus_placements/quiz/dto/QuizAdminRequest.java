package com.example.campus_placements.quiz.dto;

import java.util.List;

public class QuizAdminRequest {
    private String title;
    private String jobRole;
    private String description;
    private Integer durationMinutes;
    private Boolean active;
    private List<QuizQuestionAdminDTO> questions;

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<QuizQuestionAdminDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestionAdminDTO> questions) {
        this.questions = questions;
    }
}
