package com.example.campus_placements.quiz.dto;

import java.util.List;

public class QuizLevelAnalyticsDTO {

    private Long quizId;
    private String quizTitle;

    private int myAttemptsCount;
    private double myAverageScorePercent;
    private double myBestScorePercent;
    private Double myLastScorePercent;

    private int totalAttemptsCount;
    private double overallAverageScorePercent;
    private double overallBestScorePercent;

    private List<ScoreBucketDTO> scoreDistribution;

    public QuizLevelAnalyticsDTO() {}

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public int getMyAttemptsCount() { return myAttemptsCount; }
    public void setMyAttemptsCount(int myAttemptsCount) { this.myAttemptsCount = myAttemptsCount; }

    public double getMyAverageScorePercent() { return myAverageScorePercent; }
    public void setMyAverageScorePercent(double myAverageScorePercent) { this.myAverageScorePercent = myAverageScorePercent; }

    public double getMyBestScorePercent() { return myBestScorePercent; }
    public void setMyBestScorePercent(double myBestScorePercent) { this.myBestScorePercent = myBestScorePercent; }

    public Double getMyLastScorePercent() { return myLastScorePercent; }
    public void setMyLastScorePercent(Double myLastScorePercent) { this.myLastScorePercent = myLastScorePercent; }

    public int getTotalAttemptsCount() { return totalAttemptsCount; }
    public void setTotalAttemptsCount(int totalAttemptsCount) { this.totalAttemptsCount = totalAttemptsCount; }

    public double getOverallAverageScorePercent() { return overallAverageScorePercent; }
    public void setOverallAverageScorePercent(double overallAverageScorePercent) { this.overallAverageScorePercent = overallAverageScorePercent; }

    public double getOverallBestScorePercent() { return overallBestScorePercent; }
    public void setOverallBestScorePercent(double overallBestScorePercent) { this.overallBestScorePercent = overallBestScorePercent; }

    public List<ScoreBucketDTO> getScoreDistribution() { return scoreDistribution; }
    public void setScoreDistribution(List<ScoreBucketDTO> scoreDistribution) { this.scoreDistribution = scoreDistribution; }
}

