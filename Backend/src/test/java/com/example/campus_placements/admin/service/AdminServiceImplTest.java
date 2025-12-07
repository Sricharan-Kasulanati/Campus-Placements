package com.example.campus_placements.admin.service;

import com.example.campus_placements.admin.dto.AdminStudentResponse;
import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.quiz.repository.QuizRepository;
import com.example.campus_placements.quiz.repository.StudentQuizAttemptRepository;
import com.example.campus_placements.student.model.Registration;
import com.example.campus_placements.student.repository.RegistrationRepository;
import com.example.campus_placements.user.model.Role;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private StudentQuizAttemptRepository attemptsRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void visitStudentProfileTest() {
        Long studentId = 42L;
        User student = new User();
        student.setId(studentId);
        student.setFirstName("Shubham");
        student.setLastName("Shahare");
        student.setEmail("shubham@example.com");
        student.setRole(Role.STUDENT);

        Company c1 = new Company();
        c1.setId(100L);
        c1.setName("Google");

        Company c2 = new Company();
        c2.setId(200L);
        c2.setName("Microsoft");

        Registration r1 = new Registration();
        r1.setStudent(student);
        r1.setCompany(c1);
        r1.setRegisteredAt(Instant.parse("2025-01-01T10:00:00Z"));

        Registration r2 = new Registration();
        r2.setStudent(student);
        r2.setCompany(c2);
        r2.setRegisteredAt(Instant.parse("2025-01-02T10:00:00Z"));

        Registration r3 = new Registration();
        r3.setStudent(student);
        r3.setCompany(c1);
        r3.setRegisteredAt(Instant.parse("2025-01-03T10:00:00Z"));

        when(userRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        when(registrationRepository.findByStudentIdOrderByRegisteredAtDesc(studentId))
                .thenReturn(List.of(r3, r2, r1));

        AdminStudentResponse result = adminService.getStudent(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Shubham Shahare", result.getFullName());
        assertEquals("shubham@example.com", result.getEmail());

        assertNotNull(result.getRegisteredCompanies());
        assertEquals(2, result.getRegisteredCompanies().size());

        AdminStudentResponse.RegisteredCompanyDto comp1 = result.getRegisteredCompanies()
                .stream().filter(c -> c.getId().equals(100L)).findFirst().orElseThrow();
        assertEquals(100L, comp1.getId());
        assertEquals("Google", comp1.getName());

        AdminStudentResponse.RegisteredCompanyDto comp2 = result.getRegisteredCompanies()
                .stream().filter(c -> c.getId().equals(200L)).findFirst().orElseThrow();
        assertEquals(200L, comp2.getId());
        assertEquals("Microsoft", comp2.getName());

        verify(userRepository).findById(studentId);
        verify(registrationRepository).findByStudentIdOrderByRegisteredAtDesc(studentId);
        verifyNoMoreInteractions(userRepository, registrationRepository);
        verifyNoInteractions(quizRepository, attemptsRepository);
    }

    @Test
    void visitStudentProfileWhenStudentNotFoundTest() {
        Long studentId = 999L;
        when(userRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> adminService.getStudent(studentId));

        verify(userRepository).findById(studentId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(registrationRepository, quizRepository, attemptsRepository);
    }
}

