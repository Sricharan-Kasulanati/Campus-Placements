package com.example.campus_placements.quiz.repository;

import com.example.campus_placements.quiz.model.StudentQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentQuizAnswerRepository extends JpaRepository<StudentQuizAnswer, Long> {
}
