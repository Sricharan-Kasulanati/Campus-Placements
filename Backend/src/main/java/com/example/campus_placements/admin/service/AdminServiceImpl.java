package com.example.campus_placements.admin.service;

import com.example.campus_placements.admin.dto.AdminStudentResponse;
import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.student.model.Registration;
import com.example.campus_placements.student.repository.RegistrationRepository;
import com.example.campus_placements.user.model.Role;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
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
}

