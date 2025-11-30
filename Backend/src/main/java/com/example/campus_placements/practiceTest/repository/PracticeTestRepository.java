package com.example.campus_placements.practiceTest.repository;

import com.example.campus_placements.practiceTest.model.PracticeTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PracticeTestRepository extends JpaRepository<PracticeTest, Long> {
    List<PracticeTest> findByCompanyId(Long companyId);
    Optional<PracticeTest> findByIdAndCompanyId(Long id, Long companyId);
}
