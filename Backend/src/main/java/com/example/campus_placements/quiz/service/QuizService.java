package com.example.campus_placements.quiz.service;

import com.example.campus_placements.quiz.dto.*;
import com.example.campus_placements.quiz.model.Quiz;

import java.util.List;

public interface QuizService {

    List<QuizSummaryDTO> listQuizzesForCompany(Long companyId, String jobRole);
    QuizAdminDetailDTO createQuiz(Long companyId, QuizAdminRequest req);
    QuizAdminDetailDTO getQuizAdmin(Long quizId);
    QuizAdminDetailDTO updateQuiz(Long quizId, QuizAdminRequest req);
    void deleteQuiz(Long quizId);

    List<QuizStudentSummaryDTO> listQuizzesForCompanyStudent(Long companyId, String jobRole);
    QuizForTakingDTO getQuizForStudent(Long quizId, Long studentId);
    QuizResultDTO submitQuiz(Long quizId, Long studentId, QuizSubmitRequest req);

    Quiz findEntity(Long quizId); // optional helper if needed elsewhere
}
