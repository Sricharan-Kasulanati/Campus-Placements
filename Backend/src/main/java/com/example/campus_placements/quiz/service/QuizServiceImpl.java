package com.example.campus_placements.quiz.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.quiz.dto.*;
import com.example.campus_placements.quiz.model.Quiz;
import com.example.campus_placements.quiz.model.QuizQuestion;
import com.example.campus_placements.quiz.model.StudentQuizAttempt;
import com.example.campus_placements.quiz.repository.QuizQuestionRepository;
import com.example.campus_placements.quiz.repository.QuizRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAttemptRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizzes;
    private final QuizQuestionRepository questions;
    private final StudentQuizAttemptRepository attempts;
    private final CompanyRepository companies;

    public QuizServiceImpl(QuizRepository quizzes,
                           QuizQuestionRepository questions,
                           StudentQuizAttemptRepository attempts,
                           CompanyRepository companies) {
        this.quizzes = quizzes;
        this.questions = questions;
        this.attempts = attempts;
        this.companies = companies;
    }

    @Override
    public List<QuizSummaryDTO> listQuizzesForCompany(Long companyId, String jobRole) {
        List<Quiz> list = (jobRole != null && !jobRole.isBlank())
                ? quizzes.findByCompanyIdAndJobRole(companyId, jobRole)
                : quizzes.findByCompanyId(companyId);

        return list.stream().map(q -> {
            QuizSummaryDTO dto = new QuizSummaryDTO();
            dto.setId(q.getId());
            dto.setCompanyId(q.getCompany().getId());
            dto.setJobRole(q.getJobRole());
            dto.setTitle(q.getTitle());
            dto.setActive(q.getActive());
            dto.setQuestionCount(q.getQuestions().size());
            return dto;
        }).toList();
    }

    @Override
    public QuizAdminDetailDTO createQuiz(Long companyId, QuizAdminRequest req) {
        Company company = companies.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Quiz quiz = new Quiz();
        quiz.setCompany(company);
        quiz.setTitle(req.getTitle());
        quiz.setJobRole(req.getJobRole());
        quiz.setDescription(req.getDescription());
        quiz.setDurationMinutes(req.getDurationMinutes());
        quiz.setActive(req.getActive() == null ? true : req.getActive());

        if (req.getQuestions() != null) {
            for (QuizQuestionAdminDTO qdto : req.getQuestions()) {
                QuizQuestion qq = new QuizQuestion();
                qq.setQuestionText(qdto.getQuestionText());
                qq.setOptionA(qdto.getOptionA());
                qq.setOptionB(qdto.getOptionB());
                qq.setOptionC(qdto.getOptionC());
                qq.setOptionD(qdto.getOptionD());
                qq.setCorrectOption(Character.toUpperCase(qdto.getCorrectOption()));
                quiz.addQuestion(qq);
            }
        }

        Quiz saved = quizzes.save(quiz);
        return toAdminDetail(saved);
    }

    @Override
    public QuizAdminDetailDTO getQuizAdmin(Long quizId) {
        Quiz quiz = findEntity(quizId);
        return toAdminDetail(quiz);
    }

    @Override
    public QuizAdminDetailDTO updateQuiz(Long quizId, QuizAdminRequest req) {
        Quiz quiz = findEntity(quizId);

        quiz.setTitle(req.getTitle());
        quiz.setJobRole(req.getJobRole());
        quiz.setDescription(req.getDescription());
        quiz.setDurationMinutes(req.getDurationMinutes());
        quiz.setActive(req.getActive());

        quiz.clearQuestions();
        if (req.getQuestions() != null) {
            for (QuizQuestionAdminDTO qdto : req.getQuestions()) {
                QuizQuestion qq = new QuizQuestion();
                qq.setQuestionText(qdto.getQuestionText());
                qq.setOptionA(qdto.getOptionA());
                qq.setOptionB(qdto.getOptionB());
                qq.setOptionC(qdto.getOptionC());
                qq.setOptionD(qdto.getOptionD());
                qq.setCorrectOption(Character.toUpperCase(qdto.getCorrectOption()));
                quiz.addQuestion(qq);
            }
        }

        Quiz saved = quizzes.save(quiz);
        return toAdminDetail(saved);
    }

    @Override
    public void deleteQuiz(Long quizId) {
        quizzes.deleteById(quizId);
    }

    private QuizAdminDetailDTO toAdminDetail(Quiz q) {
        QuizAdminDetailDTO dto = new QuizAdminDetailDTO();
        dto.setId(q.getId());
        dto.setCompanyId(q.getCompany().getId());
        dto.setJobRole(q.getJobRole());
        dto.setTitle(q.getTitle());
        dto.setDescription(q.getDescription());
        dto.setDurationMinutes(q.getDurationMinutes());
        dto.setActive(q.getActive());

        dto.setQuestions(
                q.getQuestions().stream().map(qq -> {
                    QuizQuestionAdminDTO qdto = new QuizQuestionAdminDTO();
                    qdto.setId(qq.getId());
                    qdto.setQuestionText(qq.getQuestionText());
                    qdto.setOptionA(qq.getOptionA());
                    qdto.setOptionB(qq.getOptionB());
                    qdto.setOptionC(qq.getOptionC());
                    qdto.setOptionD(qq.getOptionD());
                    qdto.setCorrectOption(qq.getCorrectOption());
                    return qdto;
                }).toList()
        );

        return dto;
    }

    @Override
    public List<QuizStudentSummaryDTO> listQuizzesForCompanyStudent(Long companyId, String jobRole) {
        List<Quiz> list = (jobRole != null && !jobRole.isBlank())
                ? quizzes.findByCompanyIdAndJobRole(companyId, jobRole)
                : quizzes.findByCompanyId(companyId);

        return list.stream().filter(Quiz::getActive).map(q -> {
            QuizStudentSummaryDTO dto = new QuizStudentSummaryDTO();
            dto.setId(q.getId());
            dto.setTitle(q.getTitle());
            dto.setDescription(q.getDescription());
            dto.setDurationMinutes(q.getDurationMinutes());
            return dto;
        }).toList();
    }

    @Override
    public QuizForTakingDTO getQuizForStudent(Long quizId, Long studentId) {
        Quiz quiz = findEntity(quizId);

        QuizForTakingDTO dto = new QuizForTakingDTO();
        dto.setQuizId(quiz.getId());
        dto.setCompanyId(quiz.getCompany().getId());
        dto.setJobRole(quiz.getJobRole());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setDurationMinutes(quiz.getDurationMinutes());

        dto.setQuestions(
                quiz.getQuestions().stream().map(q -> {
                    QuizQuestionStudentDTO qdto = new QuizQuestionStudentDTO();
                    qdto.setId(q.getId());
                    qdto.setQuestionText(q.getQuestionText());
                    qdto.setOptionA(q.getOptionA());
                    qdto.setOptionB(q.getOptionB());
                    qdto.setOptionC(q.getOptionC());
                    qdto.setOptionD(q.getOptionD());
                    return qdto;
                }).toList()
        );

        return dto;
    }

    @Override
    public QuizResultDTO submitQuiz(Long quizId, Long studentId, QuizSubmitRequest req) {
        Quiz quiz = findEntity(quizId);

        Map<Long, QuizQuestion> questionMap = quiz.getQuestions().stream()
                .collect(Collectors.toMap(QuizQuestion::getId, q -> q));

        int score = 0;
        if (req.getAnswers() != null) {
            for (QuizAnswerDTO ans : req.getAnswers()) {
                QuizQuestion q = questionMap.get(ans.getQuestionId());
                if (q == null) continue;
                if (Character.toUpperCase(ans.getSelectedOption()) == q.getCorrectOption()) {
                    score++;
                }
            }
        }

        StudentQuizAttempt attempt = new StudentQuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudentId(studentId);
        attempt.setScore(score);
        attempt.setTotalQuestions(quiz.getQuestions().size());
        attempts.save(attempt);

        QuizResultDTO result = new QuizResultDTO();
        result.setScore(score);
        result.setTotalQuestions(quiz.getQuestions().size());
        return result;
    }

    @Override
    public Quiz findEntity(Long quizId) {
        return quizzes.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
    }
}
