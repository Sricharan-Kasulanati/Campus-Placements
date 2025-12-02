package com.example.campus_placements.quiz.dto;

public class ScoreBucketDTO {
    private String label;
    private int count;

    public ScoreBucketDTO() {}

    public ScoreBucketDTO(String label, int count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}

