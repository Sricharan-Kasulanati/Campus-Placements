package com.example.campus_placements.practiceTest.model;

import com.example.campus_placements.company.model.Company;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "practice_test")
public class PracticeTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 512)
    private String fileUrl;

    @Column(nullable = false)
    private long fileSize;

    @Column(nullable = false, length = 120)
    private String contentType;

    @Column(nullable = false)
    private Instant uploadedAt = Instant.now();

    @Column(name = "job_role", length = 160)
    private String jobRole;

    @Column(name = "description", columnDefinition = "text")
    private String description;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }
}

