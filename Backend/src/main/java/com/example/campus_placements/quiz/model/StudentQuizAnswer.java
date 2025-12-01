package com.example.campus_placements.quiz.model;

import jakarta.persistence.*;

@Entity
public class StudentQuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudentQuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuizQuestion question;

    private char selectedOption;
    private boolean correct;

    public Long getId() {
        return id;
    }

    public StudentQuizAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(StudentQuizAttempt attempt) {
        this.attempt = attempt;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public char getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(char selectedOption) {
        this.selectedOption = selectedOption;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}