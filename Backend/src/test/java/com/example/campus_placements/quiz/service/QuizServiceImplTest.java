package com.example.campus_placements.quiz.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.notification.service.NotificationService;
import com.example.campus_placements.quiz.dto.*;
import com.example.campus_placements.quiz.model.Quiz;
import com.example.campus_placements.quiz.model.QuizQuestion;
import com.example.campus_placements.quiz.model.StudentQuizAttempt;
import com.example.campus_placements.quiz.repository.QuizQuestionRepository;
import com.example.campus_placements.quiz.repository.QuizRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAnswerRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAttemptRepository;
import com.example.campus_placements.student.model.Registration;
import com.example.campus_placements.student.repository.RegistrationRepository;
import com.example.campus_placements.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizzes;

    @Mock
    private QuizQuestionRepository questions;

    @Mock
    private StudentQuizAttemptRepository attempts;

    @Mock
    private StudentQuizAnswerRepository answersRepo;

    @Mock
    private CompanyRepository companies;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private QuizServiceImpl service;


    @Test
    void createQuizTest() {
        Long companyId = 1L;

        Company company = new Company();
        company.setId(companyId);
        company.setName("Test Company");

        when(companies.findById(companyId)).thenReturn(Optional.of(company));

        when(quizzes.save(any(Quiz.class))).thenAnswer(invocation -> {
            Quiz q = invocation.getArgument(0);
            q.setId(100L);
            return q;
        });

        QuizQuestionAdminDTO q1 = new QuizQuestionAdminDTO();
        q1.setQuestionText("2 + 2 = ?");
        q1.setOptionA("3");
        q1.setOptionB("4");
        q1.setOptionC("5");
        q1.setOptionD("6");
        q1.setCorrectOption('b');

        QuizAdminRequest req = new QuizAdminRequest();
        req.setTitle("Java Screening Exam");
        req.setJobRole("SDE");
        req.setDescription("Basic Java + logic test");
        req.setDurationMinutes(45);
        req.setActive(true);
        req.setQuestions(List.of(q1));

        QuizAdminDetailDTO result = service.createQuiz(companyId, req);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(companyId, result.getCompanyId());
        assertEquals("SDE", result.getJobRole());
        assertEquals("Java Screening Exam", result.getTitle());
        assertEquals("Basic Java + logic test", result.getDescription());
        assertEquals(45, result.getDurationMinutes());
        assertTrue(result.getActive());

        assertNotNull(result.getQuestions());
        assertEquals(1, result.getQuestions().size());

        QuizQuestionAdminDTO dtoQuestion = result.getQuestions().get(0);
        assertEquals("2 + 2 = ?", dtoQuestion.getQuestionText());
        assertEquals("3", dtoQuestion.getOptionA());
        assertEquals("4", dtoQuestion.getOptionB());
        assertEquals("5", dtoQuestion.getOptionC());
        assertEquals("6", dtoQuestion.getOptionD());
        assertEquals('B', dtoQuestion.getCorrectOption());

        ArgumentCaptor<Quiz> quizCaptor = ArgumentCaptor.forClass(Quiz.class);
        verify(quizzes).save(quizCaptor.capture());
        Quiz saved = quizCaptor.getValue();

        assertEquals(company, saved.getCompany());
        assertEquals("Java Screening Exam", saved.getTitle());
        assertEquals("SDE", saved.getJobRole());
        assertEquals("Basic Java + logic test", saved.getDescription());
        assertEquals(45, saved.getDurationMinutes());
        assertTrue(saved.getActive());

        assertNotNull(saved.getQuestions());
        assertEquals(1, saved.getQuestions().size());
        QuizQuestion savedQ = saved.getQuestions().get(0);
        assertEquals("2 + 2 = ?", savedQ.getQuestionText());
        assertEquals("3", savedQ.getOptionA());
        assertEquals("4", savedQ.getOptionB());
        assertEquals("5", savedQ.getOptionC());
        assertEquals("6", savedQ.getOptionD());
        assertEquals('B', savedQ.getCorrectOption());

        verify(notificationService).notifyExamCreated(100L);
    }

    @BeforeEach
    void setUp() {
        service = new QuizServiceImpl(
                quizzes,
                questions,
                attempts,
                answersRepo,
                companies,
                notificationService,
                registrationRepository
        );
    }

    @Test
    void getStudentAnalyticsTest() {
        Long studentId = 1L;

        when(registrationRepository.findByStudentIdOrderByRegisteredAtDesc(studentId))
                .thenReturn(List.of());

        StudentAnalyticsOverviewDTO overview = service.getStudentAnalyticsOverview(studentId);

        assertNotNull(overview);
        assertNotNull(overview.getCompanies());
        assertTrue(overview.getCompanies().isEmpty());

        verifyNoInteractions(attempts);
        verifyNoInteractions(quizzes);
    }

    @Test
    void getStudentAnalyticsCorrectnessTest() {
        Long studentId = 1L;

        Company company = new Company();
        company.setId(101L);
        company.setName("TechCorp");

        User student = new User();
        student.setId(studentId);

        Registration reg = new Registration();
        reg.setCompany(company);
        reg.setStudent(student);

        when(registrationRepository.findByStudentIdOrderByRegisteredAtDesc(studentId))
                .thenReturn(List.of(reg));

        Quiz quiz = new Quiz();
        quiz.setId(1000L);
        quiz.setTitle("Java Basics");
        quiz.setCompany(company);

        when(quizzes.findByCompanyId(company.getId()))
                .thenReturn(List.of(quiz));

        StudentQuizAttempt myAttempt = new StudentQuizAttempt();
        myAttempt.setQuiz(quiz);
        myAttempt.setStudentId(studentId);
        myAttempt.setScore(8);
        myAttempt.setTotalQuestions(10);

        StudentQuizAttempt otherAttempt = new StudentQuizAttempt();
        otherAttempt.setQuiz(quiz);
        otherAttempt.setStudentId(999L);
        otherAttempt.setScore(5);
        otherAttempt.setTotalQuestions(10);

        when(attempts.findAll()).thenReturn(List.of(myAttempt, otherAttempt));

        StudentAnalyticsOverviewDTO overview = service.getStudentAnalyticsOverview(studentId);

        assertNotNull(overview);
        assertNotNull(overview.getCompanies());
        assertEquals(1, overview.getCompanies().size());

        CompanyLevelAnalyticsDTO companyDto = overview.getCompanies().get(0);
        assertEquals(company.getId(), companyDto.getCompanyId());
        assertEquals(company.getName(), companyDto.getCompanyName());

        assertEquals(1, companyDto.getMyAttemptsCount());
        assertEquals(80.0, companyDto.getMyAverageScorePercent());
        assertEquals(80, companyDto.getMyBestScorePercent());

        assertNotNull(companyDto.getQuizzes());
        assertEquals(1, companyDto.getQuizzes().size());

        QuizLevelAnalyticsDTO quizDto = companyDto.getQuizzes().get(0);
        assertEquals(quiz.getId(), quizDto.getQuizId());
        assertEquals(quiz.getTitle(), quizDto.getQuizTitle());

        assertEquals(1, quizDto.getMyAttemptsCount());
        assertEquals(80.0, quizDto.getMyAverageScorePercent());
        assertEquals(80, quizDto.getMyBestScorePercent());
        assertEquals(80.0, quizDto.getMyLastScorePercent());

        assertEquals(2, quizDto.getTotalAttemptsCount());
        assertEquals(65.0, quizDto.getOverallAverageScorePercent());
        assertEquals(80, quizDto.getOverallBestScorePercent());

        List<ScoreBucketDTO> buckets = quizDto.getScoreDistribution();
        assertNotNull(buckets);
        assertEquals(5, buckets.size());

        assertEquals("0–20", buckets.get(0).getLabel());
        assertEquals(0, buckets.get(0).getCount());

        assertEquals("20–40", buckets.get(1).getLabel());
        assertEquals(0, buckets.get(1).getCount());

        assertEquals("40–60", buckets.get(2).getLabel());
        assertEquals(1, buckets.get(2).getCount());

        assertEquals("60–80", buckets.get(3).getLabel());
        assertEquals(0, buckets.get(3).getCount());

        assertEquals("80–100", buckets.get(4).getLabel());
        assertEquals(1, buckets.get(4).getCount());
    }
}


