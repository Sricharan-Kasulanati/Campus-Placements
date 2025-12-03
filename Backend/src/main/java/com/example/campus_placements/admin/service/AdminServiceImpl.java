package com.example.campus_placements.admin.service;

import com.example.campus_placements.admin.dto.AdminAnalyticsOverviewDTO;
import com.example.campus_placements.admin.dto.AdminCompanyAnalyticsDTO;
import com.example.campus_placements.admin.dto.AdminStudentResponse;
import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.quiz.model.StudentQuizAttempt;
import com.example.campus_placements.quiz.repository.QuizRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAttemptRepository;
import com.example.campus_placements.student.model.Registration;
import com.example.campus_placements.student.repository.RegistrationRepository;
import com.example.campus_placements.user.model.Role;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;
    private final CompanyRepository companies;
    private final QuizRepository quizzes;
    private final StudentQuizAttemptRepository attempts;

    public AdminServiceImpl(UserRepository userRepository,
                            RegistrationRepository registrationRepository, CompanyRepository companies, QuizRepository quizzes, StudentQuizAttemptRepository attempts) {
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
        this.companies = companies;
        this.quizzes = quizzes;
        this.attempts = attempts;
    }

    @Override
    public List<AdminStudentResponse> listAllStudents() {
        List<User> students = userRepository.findByRole(Role.STUDENT);
        return students.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminStudentResponse getStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        return toResponse(student);
    }

    private AdminStudentResponse toResponse(User student) {
        AdminStudentResponse dto = new AdminStudentResponse();
        dto.setId(student.getId());

        String fullName = student.getFirstName() + " " + student.getLastName();
        dto.setFullName(fullName.trim());
        dto.setEmail(student.getEmail());

        List<Registration> regs =
                registrationRepository.findByStudentIdOrderByRegisteredAtDesc(student.getId());

        List<AdminStudentResponse.RegisteredCompanyDto> companyDtos = regs.stream()
                .map(Registration::getCompany)
                .distinct()
                .map(this::toCompanyDto)
                .collect(Collectors.toList());

        dto.setRegisteredCompanies(companyDtos);
        return dto;
    }

    private AdminStudentResponse.RegisteredCompanyDto toCompanyDto(Company company) {
        return new AdminStudentResponse.RegisteredCompanyDto(company.getId(), company.getName());
    }

    public AdminAnalyticsOverviewDTO getOverview() {
        AdminAnalyticsOverviewDTO dto = new AdminAnalyticsOverviewDTO();
        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long totalCompanies = companies.count();
        long totalQuizzes = quizzes.count();
        long totalAttempts = attempts.count();

        dto.setTotalStudents(totalStudents);
        dto.setTotalCompanies(totalCompanies);
        dto.setTotalQuizzes(totalQuizzes);
        dto.setTotalAttempts(totalAttempts);

        List<AdminCompanyAnalyticsDTO> companyDtos = new ArrayList<>();
        for (Company c : companies.findAll()) {
            AdminCompanyAnalyticsDTO cdto = new AdminCompanyAnalyticsDTO();
            cdto.setCompanyId(c.getId());
            cdto.setCompanyName(c.getName());

            long regCount = registrationRepository.countDistinctStudentIdByCompanyId(c.getId());
            cdto.setRegisteredStudents(regCount);

            List<StudentQuizAttempt> companyAttempts = attempts.findByQuiz_Company_Id(c.getId());

            cdto.setTotalAttempts(companyAttempts.size());

            int scoreSum = 0;
            int passCount = 0;

            for (StudentQuizAttempt a : companyAttempts) {
                int total = a.getTotalQuestions();
                int percent = total > 0 ? (a.getScore() * 100 / total) : 0;
                scoreSum += percent;
                if (percent >= 50) passCount++;
            }

            double avgScore = companyAttempts.isEmpty()
                    ? 0.0
                    : (scoreSum * 1.0 / companyAttempts.size());

            double passRate50 = companyAttempts.isEmpty()
                    ? 0.0
                    : (passCount * 100.0 / companyAttempts.size());

            cdto.setAvgScorePercent(avgScore);
            cdto.setPassRate50Percent(passRate50);

            companyDtos.add(cdto);
        }

        dto.setCompanies(companyDtos);
        return dto;
    }
}

