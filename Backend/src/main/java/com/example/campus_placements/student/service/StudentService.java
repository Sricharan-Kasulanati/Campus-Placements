package com.example.campus_placements.student.service;

import com.example.campus_placements.company.model.Company;
import java.util.List;

public interface StudentService {
    void register(Long studentId, Long companyId);
    void unregister(Long studentId, Long companyId);
    List<Company> listRegisteredCompanies(Long studentId);
    List<Long> registeredCompanyIds(Long studentId);
}
