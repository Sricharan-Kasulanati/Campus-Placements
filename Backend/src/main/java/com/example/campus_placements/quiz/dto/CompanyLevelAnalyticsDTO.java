package com.example.campus_placements.quiz.dto;

import java.util.List;

public class CompanyLevelAnalyticsDTO {

    private Long companyId;
    private String companyName;

    private int myAttemptsCount;
    private double myAverageScorePercent;
    private double myBestScorePercent;

    private List<QuizLevelAnalyticsDTO> quizzes;

    public CompanyLevelAnalyticsDTO() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public int getMyAttemptsCount() { return myAttemptsCount; }
    public void setMyAttemptsCount(int myAttemptsCount) { this.myAttemptsCount = myAttemptsCount; }

    public double getMyAverageScorePercent() { return myAverageScorePercent; }
    public void setMyAverageScorePercent(double myAverageScorePercent) { this.myAverageScorePercent = myAverageScorePercent; }

    public double getMyBestScorePercent() { return myBestScorePercent; }
    public void setMyBestScorePercent(double myBestScorePercent) { this.myBestScorePercent = myBestScorePercent; }

    public List<QuizLevelAnalyticsDTO> getQuizzes() { return quizzes; }
    public void setQuizzes(List<QuizLevelAnalyticsDTO> quizzes) { this.quizzes = quizzes; }
}

