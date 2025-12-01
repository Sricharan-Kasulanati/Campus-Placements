package com.example.campus_placements.quiz.dto;

import java.util.List;

public class QuizResultDTO {
    private int score;
    private int totalQuestions;
    private List<QuestionResultDTO> questions;
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<QuestionResultDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResultDTO> questions) {
        this.questions = questions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
