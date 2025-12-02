package com.example.campus_placements.quiz.controller;

import com.example.campus_placements.quiz.dto.*;
import com.example.campus_placements.quiz.service.QuizService;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizzes;
    private final UserRepository users;

    public QuizController(QuizService quizzes, UserRepository users) {
        this.quizzes = quizzes;
        this.users = users;
    }

    private Long currentUserId(UserDetails me) {
        User u = users.findByEmail(me.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return u.getId();
    }

    @GetMapping("/admin/companies/{companyId}/quizzes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<QuizSummaryDTO> listQuizzesForCompany(
            @PathVariable Long companyId,
            @RequestParam(required = false) String jobRole
    ) {
        return quizzes.listQuizzesForCompany(companyId, jobRole);
    }

    @PostMapping("/admin/companies/{companyId}/quizzes")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizAdminDetailDTO createQuiz(
            @PathVariable Long companyId,
            @RequestBody QuizAdminRequest req
    ) {
        return quizzes.createQuiz(companyId, req);
    }

    @GetMapping("/admin/quizzes/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    public QuizAdminDetailDTO getQuizAdmin(@PathVariable Long quizId) {
        return quizzes.getQuizAdmin(quizId);
    }

    @PutMapping("/admin/quizzes/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    public QuizAdminDetailDTO updateQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizAdminRequest req
    ) {
        return quizzes.updateQuiz(quizId, req);
    }

    @DeleteMapping("/admin/quizzes/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable Long quizId) {
        quizzes.deleteQuiz(quizId);
    }

    @GetMapping("/companies/{companyId}/quizzes")
    @PreAuthorize("hasRole('STUDENT')")
    public List<QuizStudentSummaryDTO> listQuizzesForStudent(
            @PathVariable Long companyId,
            @RequestParam(required = false) String jobRole,
            @AuthenticationPrincipal UserDetails me
    ) {
        Long studentId = currentUserId(me);
        return quizzes.listQuizzesForCompanyStudent(companyId, jobRole);
    }

    @GetMapping("/quizzes/{quizId}/take")
    @PreAuthorize("hasRole('STUDENT')")
    public QuizForTakingDTO getQuizForStudent(
            @PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails me
    ) {
        Long studentId = currentUserId(me);
        return quizzes.getQuizForStudent(quizId, studentId);
    }

    @PostMapping("/quizzes/{quizId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public QuizResultDTO submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmitRequest req,
            @AuthenticationPrincipal UserDetails me
    ) {
        Long studentId = currentUserId(me);
        return quizzes.submitQuiz(quizId, studentId, req);
    }

    @GetMapping("/students/me/quiz-analytics/overview")
    @PreAuthorize("hasRole('STUDENT')")
    public StudentAnalyticsOverviewDTO myAnalyticsOverview(@AuthenticationPrincipal UserDetails me) {
        Long studentId = currentUserId(me);
        return quizzes.getStudentAnalyticsOverview(studentId);
    }
}
