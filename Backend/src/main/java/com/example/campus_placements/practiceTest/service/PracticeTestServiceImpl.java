package com.example.campus_placements.practiceTest.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.notification.service.NotificationService;
import com.example.campus_placements.practiceTest.dto.PracticeTestResponse;
import com.example.campus_placements.practiceTest.model.PracticeTest;
import com.example.campus_placements.practiceTest.repository.PracticeTestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PracticeTestServiceImpl implements PracticeTestService {

    private final PracticeTestRepository repository;
    private final CompanyRepository companyRepository;
    private final NotificationService notificationService;

    public PracticeTestServiceImpl(PracticeTestRepository repository,
                                   CompanyRepository companyRepository, NotificationService notificationService) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PracticeTestResponse> listForCompany(Long companyId) {
        return repository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PracticeTestResponse upload(Long companyId,
                                       String title,
                                       String jobRole,
                                       String description,
                                       MultipartFile file) throws IOException {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company not found: " + companyId));

        PracticeTest pt = new PracticeTest();
        pt.setCompany(company);
        pt.setTitle(title);
        pt.setJobRole(jobRole);
        pt.setDescription(description);
        pt.setFileName(file.getOriginalFilename());
        pt.setContentType(file.getContentType() != null
                ? file.getContentType()
                : "application/pdf");
        pt.setFileSize(file.getSize());
        pt.setUploadedAt(Instant.now());
        pt.setData(file.getBytes());

        PracticeTest saved = repository.save(pt);
        notificationService.notifyPrepAdded(companyId);
        return toResponse(saved);
    }

    @Override
    public void delete(Long companyId, Long practiceTestId) {
        PracticeTest pt = repository.findByIdAndCompanyId(practiceTestId, companyId)
                .orElseThrow(() -> new NoSuchElementException("Practice test not found"));
        repository.delete(pt);
    }

    @Override
    @Transactional(readOnly = true)
    public PracticeTest getPracticeTest(Long companyId, Long practiceTestId) {
        return repository.findByIdAndCompanyId(practiceTestId, companyId)
                .orElseThrow(() -> new NoSuchElementException("Practice test not found"));
    }

    private PracticeTestResponse toResponse(PracticeTest e) {
        PracticeTestResponse dto = new PracticeTestResponse();
        dto.setId(e.getId());
        dto.setCompanyId(e.getCompany().getId());
        dto.setTitle(e.getTitle());
        dto.setJobRole(e.getJobRole());
        dto.setDescription(e.getDescription());
        dto.setFileSize(e.getFileSize());
        dto.setContentType(e.getContentType());
        dto.setUploadedAt(e.getUploadedAt());
        String url = String.format("/api/companies/%d/practice-tests/%d/file",
                e.getCompany().getId(), e.getId());
        dto.setFileUrl(url);

        return dto;
    }
}
