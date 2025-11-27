package com.example.campus_placements.student.model;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.user.model.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "registrations",
        uniqueConstraints = @UniqueConstraint(name = "uk_registration_student_company",
                columnNames = {"student_id","company_id"}),
        indexes = {
                @Index(name = "idx_registration_student", columnList = "student_id"),
                @Index(name = "idx_registration_company", columnList = "company_id")
        }
)
public class Registration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;             // must have role STUDENT

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, updatable = false)
    private Instant registeredAt = Instant.now();

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public Instant getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Instant t) { this.registeredAt = t; }
}
