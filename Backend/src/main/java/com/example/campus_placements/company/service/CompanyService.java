package com.example.campus_placements.company.service;

import com.example.campus_placements.company.dto.*;
import com.example.campus_placements.company.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Company getCompanyById(Long id);
    Page<Company> listCompanies(Pageable pageable);
    Page<Company> searchCompanies(CompanySearchCriteria criteria, Pageable pageable);
    Company createCompany(CompanyCreateRequest req);
    Company updateCompany(CompanyUpdateRequest req);
}
