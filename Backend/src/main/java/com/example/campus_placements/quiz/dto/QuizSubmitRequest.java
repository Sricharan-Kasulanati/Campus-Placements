package com.example.campus_placements.quiz.dto;

import java.util.List;

public class QuizSubmitRequest {
    private List<QuizAnswerDTO> answers;

    public List<QuizAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswerDTO> answers) {
        this.answers = answers;
    }
}
