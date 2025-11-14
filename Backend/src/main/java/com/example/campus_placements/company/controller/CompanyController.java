package com.example.campus_placements.company.controller;

import com.example.campus_placements.company.dto.CompanyCreateRequest;
import com.example.campus_placements.company.dto.CompanySearchCriteria;
import com.example.campus_placements.company.dto.CompanyUpdateRequest;
import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) { this.service = service; }

    @GetMapping
    public Page<Company> listOfCompanies(@PageableDefault(size = 12, sort = "name") Pageable pageable) {
        return service.listCompanies(pageable);
    }

    @PostMapping("/search")
    public Page<Company> searchCompanies(@RequestBody CompanySearchCriteria criteria,
                                @PageableDefault(size = 12, sort = "name") Pageable pageable) {
        return service.searchCompanies(criteria, pageable);
    }

    @GetMapping("/{id}")
    public Company searchCompanyById(@PathVariable Long id) {
        return service.getCompanyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Company createCompany(@Valid @RequestBody CompanyCreateRequest req) {
        return service.createCompany(req);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Company updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyUpdateRequest req) {
        req.setId(id);
        return service.updateCompany(req);
    }
}
