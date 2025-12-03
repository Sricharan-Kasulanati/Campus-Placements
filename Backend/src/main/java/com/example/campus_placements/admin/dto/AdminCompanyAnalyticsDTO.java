package com.example.campus_placements.admin.dto;

public class AdminCompanyAnalyticsDTO {

    private Long companyId;
    private String companyName;
    private long registeredStudents;
    private long totalAttempts;
    private double avgScorePercent;
    private double passRate50Percent;

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public long getRegisteredStudents() { return registeredStudents; }
    public void setRegisteredStudents(long registeredStudents) { this.registeredStudents = registeredStudents; }

    public long getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(long totalAttempts) { this.totalAttempts = totalAttempts; }

    public double getAvgScorePercent() { return avgScorePercent; }
    public void setAvgScorePercent(double avgScorePercent) { this.avgScorePercent = avgScorePercent; }

    public double getPassRate50Percent() { return passRate50Percent; }
    public void setPassRate50Percent(double passRate50Percent) { this.passRate50Percent = passRate50Percent; }
}

