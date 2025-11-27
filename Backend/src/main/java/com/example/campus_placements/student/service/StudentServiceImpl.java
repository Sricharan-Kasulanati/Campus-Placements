package com.example.campus_placements.student.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.student.model.Registration;
import com.example.campus_placements.student.repository.RegistrationRepository;
import com.example.campus_placements.user.model.User;
import com.example.campus_placements.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final RegistrationRepository regs;
    private final CompanyRepository companies;
    private final UserRepository users;

    public StudentServiceImpl(RegistrationRepository regs,
                              CompanyRepository companies,
                              UserRepository users) {
        this.regs = regs; this.companies = companies; this.users = users;
    }

    @Override @Transactional
    public void register(Long studentId, Long companyId) {
        if (regs.existsByStudentIdAndCompanyId(studentId, companyId)) return;
        User student = users.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        Company company = companies.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));
        Registration r = new Registration();
        r.setStudent(student);
        r.setCompany(company);
        regs.save(r);
    }

    @Override @Transactional
    public void unregister(Long studentId, Long companyId) {
        var r = regs.findByStudentIdAndCompanyId(studentId, companyId)
                .orElseThrow(() -> new IllegalArgumentException("Not registered"));
        regs.delete(r);
    }

    @Override
    public List<Company> listRegisteredCompanies(Long studentId) {
        return regs.findByStudentIdOrderByRegisteredAtDesc(studentId)
                .stream().map(Registration::getCompany).toList();
    }

    @Override
    public List<Long> registeredCompanyIds(Long studentId) {
        return regs.findCompanyIdsByStudent(studentId);
    }
}
