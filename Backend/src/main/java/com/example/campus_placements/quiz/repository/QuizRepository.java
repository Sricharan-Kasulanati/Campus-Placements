package com.example.campus_placements.quiz.repository;

import com.example.campus_placements.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByCompanyId(Long companyId);

    List<Quiz> findByCompanyIdAndJobRole(Long companyId, String jobRole);
}
