package com.example.campus_placements.quiz.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.notification.service.NotificationService;
import com.example.campus_placements.quiz.dto.*;
import com.example.campus_placements.quiz.model.Quiz;
import com.example.campus_placements.quiz.model.QuizQuestion;
import com.example.campus_placements.quiz.model.StudentQuizAnswer;
import com.example.campus_placements.quiz.model.StudentQuizAttempt;
import com.example.campus_placements.quiz.repository.QuizQuestionRepository;
import com.example.campus_placements.quiz.repository.QuizRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAnswerRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAttemptRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizzes;
    private final QuizQuestionRepository questions;
    private final StudentQuizAttemptRepository attempts;
    private final StudentQuizAnswerRepository answersRepo;
    private final CompanyRepository companies;
    private final NotificationService notificationService;

    public QuizServiceImpl(QuizRepository quizzes,
                           QuizQuestionRepository questions,
                           StudentQuizAttemptRepository attempts, StudentQuizAnswerRepository answersRepo,
                           CompanyRepository companies, NotificationService notificationService) {
        this.quizzes = quizzes;
        this.questions = questions;
        this.attempts = attempts;
        this.answersRepo = answersRepo;
        this.companies = companies;
        this.notificationService = notificationService;
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
        notificationService.notifyExamCreated(saved.getId());
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

        Map<Long, QuizQuestion> existingById = quiz.getQuestions().stream()
                .collect(Collectors.toMap(QuizQuestion::getId, q -> q));

        if (req.getQuestions() != null) {
            for (QuizQuestionAdminDTO qdto : req.getQuestions()) {

                QuizQuestion qq = null;

                if (qdto.getId() != null) {
                    qq = existingById.get(qdto.getId());
                }

                if (qq == null) {
                    qq = new QuizQuestion();
                    qq.setQuiz(quiz);
                    quiz.addQuestion(qq);
                }
                qq.setQuestionText(qdto.getQuestionText());
                qq.setOptionA(qdto.getOptionA());
                qq.setOptionB(qdto.getOptionB());
                qq.setOptionC(qdto.getOptionC());
                qq.setOptionD(qdto.getOptionD());
                qq.setCorrectOption(Character.toUpperCase(qdto.getCorrectOption()));
            }
        }

        Quiz saved = quizzes.save(quiz);
        notificationService.notifyExamUpdated(saved.getId());
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

        Map<Long, Character> answerMap = new HashMap<>();
        if (req.getAnswers() != null) {
            for (QuizAnswerDTO ans : req.getAnswers()) {
                answerMap.put(ans.getQuestionId(), ans.getSelectedOption());
            }
        }

        int score = 0;
        List<QuestionResultDTO> questionResults = new ArrayList<>();
        List<StudentQuizAnswer> answerEntities = new ArrayList<>();

        for (QuizQuestion q : quiz.getQuestions()) {
            char correctOption = q.getCorrectOption();
            Character selected = answerMap.get(q.getId());
            boolean isCorrect = selected != null &&
                    Character.toUpperCase(selected) == Character.toUpperCase(correctOption);

            if (isCorrect) {
                score++;
            }
            QuestionResultDTO qr = new QuestionResultDTO();
            qr.setQuestionId(q.getId());
            qr.setQuestionText(q.getQuestionText());
            qr.setOptionA(q.getOptionA());
            qr.setOptionB(q.getOptionB());
            qr.setOptionC(q.getOptionC());
            qr.setOptionD(q.getOptionD());
            qr.setCorrectOption(String.valueOf(correctOption));
            qr.setSelectedOption(selected != null ? String.valueOf(selected) : null);
            qr.setCorrect(isCorrect);
            questionResults.add(qr);
        }
        StudentQuizAttempt attempt = new StudentQuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudentId(studentId);
        attempt.setScore(score);
        attempt.setTotalQuestions(quiz.getQuestions().size());
        attempt = attempts.save(attempt);

        if (req.getAnswers() != null) {
            for (QuizAnswerDTO ans : req.getAnswers()) {
                QuizQuestion q = questionMap.get(ans.getQuestionId());
                if (q == null) continue;

                char selected = ans.getSelectedOption();
                boolean isCorrect = Character.toUpperCase(selected) ==
                        Character.toUpperCase(q.getCorrectOption());

                StudentQuizAnswer a = new StudentQuizAnswer();
                a.setAttempt(attempt);
                a.setQuestion(q);
                a.setSelectedOption(selected);
                a.setCorrect(isCorrect);
                answerEntities.add(a);
            }
            answersRepo.saveAll(answerEntities);
        }

        QuizResultDTO result = new QuizResultDTO();
        result.setScore(score);
        result.setTotalQuestions(quiz.getQuestions().size());
        result.setQuestions(questionResults);
        return result;
    }

    @Override
    public Quiz findEntity(Long quizId) {
        return quizzes.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
    }

    @Override
    public StudentAnalyticsOverviewDTO getStudentAnalyticsOverview(Long studentId) {
        List<StudentQuizAttempt> allAttempts = attempts.findAll();
        List<StudentQuizAttempt> myAttempts = allAttempts.stream()
                .filter(a -> Objects.equals(a.getStudentId(), studentId))
                .toList();

        if (myAttempts.isEmpty()) {
            return new StudentAnalyticsOverviewDTO(Collections.emptyList());
        }
        Map<Long, List<StudentQuizAttempt>> myByCompany =
                myAttempts.stream().collect(groupingBy(a -> a.getQuiz().getCompany().getId()));
        Map<Long, List<StudentQuizAttempt>> allByQuiz =
                allAttempts.stream().collect(groupingBy(a -> a.getQuiz().getId()));

        List<CompanyLevelAnalyticsDTO> companies = new ArrayList<>();

        for (Map.Entry<Long, List<StudentQuizAttempt>> entry : myByCompany.entrySet()) {
            Long companyId = entry.getKey();
            List<StudentQuizAttempt> myCompanyAttempts = entry.getValue();

            Company company = myCompanyAttempts.get(0).getQuiz().getCompany();
            int companyAttemptsCount = 0;
            int companyPercentSum = 0;
            int companyBestPercent = 0;
            Map<Long, List<StudentQuizAttempt>> myCompanyByQuiz =
                    myCompanyAttempts.stream().collect(groupingBy(a -> a.getQuiz().getId()));

            List<QuizLevelAnalyticsDTO> quizDTOs = new ArrayList<>();

            for (Map.Entry<Long, List<StudentQuizAttempt>> qEntry : myCompanyByQuiz.entrySet()) {
                Long quizId = qEntry.getKey();
                List<StudentQuizAttempt> myQuizAttempts = qEntry.getValue();
                Quiz quiz = myQuizAttempts.get(0).getQuiz();

                int myCount = myQuizAttempts.size();
                int mySumPercent = 0;
                int myBestPercent = 0;
                Double myLastPercent = null;

                for (StudentQuizAttempt a : myQuizAttempts) {
                    int total = a.getTotalQuestions();
                    int percent = total > 0 ? (a.getScore() * 100 / total) : 0;
                    mySumPercent += percent;
                    myBestPercent = Math.max(myBestPercent, percent);
                    myLastPercent = (double) percent;
                }

                double myAvgPercent = myCount > 0 ? (mySumPercent * 1.0 / myCount) : 0.0;

                companyAttemptsCount += myCount;
                companyPercentSum += mySumPercent;
                companyBestPercent = Math.max(companyBestPercent, myBestPercent);

                List<StudentQuizAttempt> allQuizAttempts = allByQuiz.getOrDefault(quizId, List.of());

                int allCount = allQuizAttempts.size();
                int allSumPercent = 0;
                int allBestPercent = 0;

                int[] buckets = new int[5];

                for (StudentQuizAttempt a : allQuizAttempts) {
                    int total = a.getTotalQuestions();
                    int percent = total > 0 ? (a.getScore() * 100 / total) : 0;

                    allSumPercent += percent;
                    allBestPercent = Math.max(allBestPercent, percent);

                    int idx = Math.min(4, percent / 20);
                    buckets[idx]++;
                }

                double allAvgPercent = allCount > 0 ? (allSumPercent * 1.0 / allCount) : 0.0;

                List<ScoreBucketDTO> bucketDTOs = List.of(
                        new ScoreBucketDTO("0–20", buckets[0]),
                        new ScoreBucketDTO("20–40", buckets[1]),
                        new ScoreBucketDTO("40–60", buckets[2]),
                        new ScoreBucketDTO("60–80", buckets[3]),
                        new ScoreBucketDTO("80–100", buckets[4])
                );

                QuizLevelAnalyticsDTO quizDTO = new QuizLevelAnalyticsDTO();
                quizDTO.setQuizId(quizId);
                quizDTO.setQuizTitle(quiz.getTitle());
                quizDTO.setMyAttemptsCount(myCount);
                quizDTO.setMyAverageScorePercent(myAvgPercent);
                quizDTO.setMyBestScorePercent(myBestPercent);
                quizDTO.setMyLastScorePercent(myLastPercent);
                quizDTO.setTotalAttemptsCount(allCount);
                quizDTO.setOverallAverageScorePercent(allAvgPercent);
                quizDTO.setOverallBestScorePercent(allBestPercent);
                quizDTO.setScoreDistribution(bucketDTOs);

                quizDTOs.add(quizDTO);
            }

            CompanyLevelAnalyticsDTO companyDTO = new CompanyLevelAnalyticsDTO();
            companyDTO.setCompanyId(companyId);
            companyDTO.setCompanyName(company.getName());
            companyDTO.setMyAttemptsCount(companyAttemptsCount);
            companyDTO.setMyAverageScorePercent(
                    companyAttemptsCount > 0 ? (companyPercentSum * 1.0 / companyAttemptsCount) : 0.0
            );
            companyDTO.setMyBestScorePercent(companyBestPercent);
            companyDTO.setQuizzes(quizDTOs);

            companies.add(companyDTO);
        }

        return new StudentAnalyticsOverviewDTO(companies);
    }
}
