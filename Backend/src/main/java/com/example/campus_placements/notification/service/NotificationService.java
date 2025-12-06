package com.example.campus_placements.notification.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.quiz.model.Quiz;
import com.example.campus_placements.quiz.repository.QuizRepository;
import com.example.campus_placements.student.repository.RegistrationRepository;
import com.example.campus_placements.user.model.Role;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    private final UserRepository users;
    private final RegistrationRepository registrations;
    private final CompanyRepository companies;
    private final QuizRepository quizzes;
    private final EmailService email;

    public NotificationService(UserRepository users,
                               RegistrationRepository registrations,
                               CompanyRepository companies,
                               QuizRepository quizzes,
                               EmailService email) {
        this.users = users;
        this.registrations = registrations;
        this.companies = companies;
        this.quizzes = quizzes;
        this.email = email;
    }

    public void notifyAllStudentsCompanyCreated(Company company) {
        List<User> studentUsers = users.findByRole(Role.STUDENT);

        String subject = "New company added: " + company.getName();
        String body = """
                A new company has been added on the Campus Placements portal.

                Company: %s
                Location: %s
                Category: %s

                Log in to the portal to view details and register.
                """.formatted(
                company.getName(),
                nullSafe(company.getLocation()),
                nullSafe(company.getCategory())
        );
        List<String> emails = emailsForCompany(company.getId());
        for (String emailAddr : emails) {
                email.send(emailAddr, subject, body);
                break;
        }
    }

    private List<String> emailsForCompany(Long companyId) {
        return registrations.findEmailsByCompanyId(companyId).stream()
                .filter(Objects::nonNull)
                .filter(e -> !e.isBlank())
                .distinct()
                .toList();
    }

    public void notifyCompanyUpdated(Long companyId) {
        Company company = companies.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        String subject = "Company details updated: " + company.getName();
        String body = """
                The company you registered for has updated its details.

                Company: %s

                Please log in to the portal to review the latest information.
                """.formatted(company.getName());

        List<String> emails = emailsForCompany(company.getId());

        for (String emailAddr : emails) {
                email.send(emailAddr, subject, body);
                break;
        }


    }

    public void notifyPrepAdded(Long companyId) {
        Company company = companies.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        String subject = "New prep papers for " + company.getName();
        String body = """
                New preparation material has been added for the company you registered for.

                Company: %s

                Log in to the portal and check the Prep section.
                """.formatted(company.getName());

        List<String> emails = emailsForCompany(company.getId());

        for (String emailAddr : emails) {
            email.send(emailAddr, subject, body);
            break;
        }


    }

    public void notifyExamCreated(Long quizId) {
        Quiz quiz = quizzes.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        Company company = quiz.getCompany();

        String subject = "New practice exam for " + company.getName();
        String body = """
                A new practice exam has been added.

                Company: %s
                Exam: %s
                Role: %s
                Duration: %d minutes

                Log in to the portal to attempt the test.
                """.formatted(
                company.getName(),
                quiz.getTitle(),
                nullSafe(quiz.getJobRole()),
                quiz.getDurationMinutes()
        );

        List<String> emails = emailsForCompany(company.getId());

        for (String emailAddr : emails) {
            email.send(emailAddr, subject, body);
            break;
        }


    }

    public void notifyExamUpdated(Long quizId) {
        Quiz quiz = quizzes.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        Company company = quiz.getCompany();

        String subject = "Practice exam updated for " + company.getName();
        String body = """
                A practice exam has been updated.

                Company: %s
                Exam: %s
                Role: %s

                Some questions or options may have changed. Please review before attempting.
                """.formatted(
                company.getName(),
                quiz.getTitle(),
                nullSafe(quiz.getJobRole())
        );

        List<String> emails = emailsForCompany(company.getId());

        for (String emailAddr : emails) {
            email.send(emailAddr, subject, body);
            break;
        }

    }

    private String nullSafe(String s) {
        return s == null ? "-" : s;
    }
}
