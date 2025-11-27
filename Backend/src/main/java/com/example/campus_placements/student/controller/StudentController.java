package com.example.campus_placements.student.controller;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.student.service.StudentService;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;
    private final UserRepository users;

    public StudentController(StudentService service, UserRepository users) {
        this.service = service; this.users = users;
    }

    private Long currentStudentId(UserDetails me) {
        User u = users.findByEmail(me.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return u.getId();
    }

    @PostMapping("/me/registrations/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('STUDENT')")
    public void register(@AuthenticationPrincipal UserDetails me,
                         @PathVariable Long companyId) {
        service.register(currentStudentId(me), companyId);
    }

    @DeleteMapping("/me/registrations/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('STUDENT')")
    public void unregister(@AuthenticationPrincipal UserDetails me,
                           @PathVariable Long companyId) {
        service.unregister(currentStudentId(me), companyId);
    }

    @GetMapping("/me/registrations")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Company> myCompanies(@AuthenticationPrincipal UserDetails me) {
        return service.listRegisteredCompanies(currentStudentId(me));
    }

    @GetMapping("/me/registrations/ids")
    @PreAuthorize("hasRole('STUDENT')")
    public List<Long> myCompanyIds(@AuthenticationPrincipal UserDetails me) {
        return service.registeredCompanyIds(currentStudentId(me));
    }
}
