package com.example.campus_placements.quiz.dto;

import java.util.List;

public class QuizForTakingDTO {
    private Long quizId;
    private Long companyId;
    private String jobRole;
    private String title;
    private String description;
    private Integer durationMinutes;
    private List<QuizQuestionStudentDTO> questions;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public List<QuizQuestionStudentDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestionStudentDTO> questions) {
        this.questions = questions;
    }
}
