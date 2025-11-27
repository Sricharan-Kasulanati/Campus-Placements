package com.example.campus_placements.company.service;

import com.example.campus_placements.company.dto.CompanyCreateRequest;
import com.example.campus_placements.company.dto.CompanySearchCriteria;
import com.example.campus_placements.company.dto.CompanyUpdateRequest;
import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repo;

    public CompanyServiceImpl(CompanyRepository repo)
    {
        this.repo = repo;
    }

    @Override
    public Company getCompanyById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
    }

    @Override
    public Page<Company> listCompanies(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Page<Company> searchCompanies(CompanySearchCriteria criteria, Pageable pageable) {
        String kw  = blankToNull(criteria.getKeyword());
        String cat = blankToNull(criteria.getCategory());
        String loc = blankToNull(criteria.getLocation());
        return repo.search(kw, cat, loc, pageable);
    }

    @Override
    public Company createCompany(CompanyCreateRequest req) {
        Company c = new Company();
        c.setName(req.getName());
        c.setDescription(req.getDescription());
        c.setLocation(req.getLocation());
        c.setWebsite(req.getWebsite());
        c.setCategory(req.getCategory());
        return repo.save(c);
    }

    @Override
    public Company updateCompany(CompanyUpdateRequest req) {
        Company c = repo.findById(req.getId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + req.getId()));
        c.setName(req.getName());
        c.setDescription(req.getDescription());
        c.setLocation(req.getLocation());
        c.setWebsite(req.getWebsite());
        c.setCategory(req.getCategory());
        return repo.save(c);
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
