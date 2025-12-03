package com.example.campus_placements.quiz.repository;

import com.example.campus_placements.quiz.model.StudentQuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentQuizAttemptRepository extends JpaRepository<StudentQuizAttempt, Long> {

    List<StudentQuizAttempt> findByQuizIdAndStudentId(Long quizId, Long studentId);
    List<StudentQuizAttempt> findByQuiz_Company_Id(Long companyId);
}
