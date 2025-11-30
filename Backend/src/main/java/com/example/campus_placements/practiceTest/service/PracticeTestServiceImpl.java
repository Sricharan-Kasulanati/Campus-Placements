package com.example.campus_placements.practiceTest.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.practiceTest.dto.PracticeTestResponse;
import com.example.campus_placements.practiceTest.model.PracticeTest;
import com.example.campus_placements.practiceTest.repository.PracticeTestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PracticeTestServiceImpl implements PracticeTestService {
    private final CompanyRepository companyRepository;
    private final PracticeTestRepository practiceTestRepository;

    private final Path baseDir = Paths.get("uploads", "practice-tests");

    public PracticeTestServiceImpl(CompanyRepository companyRepository,
                                          PracticeTestRepository practiceTestRepository) {
        this.companyRepository = companyRepository;
        this.practiceTestRepository = practiceTestRepository;
    }

    @Override
    public List<PracticeTestResponse> listForCompany(Long companyId) {
        return practiceTestRepository.findByCompanyId(companyId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public PracticeTestResponse upload(Long companyId, String title,String jobRole,
                                       String description, MultipartFile file) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        Files.createDirectories(baseDir.resolve(String.valueOf(companyId)));

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "practice.pdf";
        Path target = baseDir.resolve(String.valueOf(companyId)).resolve(originalName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String url = "/files/practice-tests/" + companyId + "/" + originalName;

        PracticeTest entity = new PracticeTest();
        entity.setCompany(company);
        entity.setTitle(title);
        entity.setFileUrl(url);
        entity.setFileSize(file.getSize());
        entity.setContentType(file.getContentType() != null ? file.getContentType() : "application/pdf");

        PracticeTest saved = practiceTestRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public void delete(Long companyId, Long practiceTestId) {
        PracticeTest entity = practiceTestRepository.findById(practiceTestId)
                .orElseThrow(() -> new IllegalArgumentException("Practice test not found: " + practiceTestId));

        if (!entity.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("Practice test does not belong to company " + companyId);
        }

        practiceTestRepository.delete(entity);

    }

    private PracticeTestResponse toDto(PracticeTest e) {
        PracticeTestResponse dto = new PracticeTestResponse();
        dto.setId(e.getId());
        dto.setCompanyId(e.getCompany().getId());
        dto.setTitle(e.getTitle());
        dto.setFileUrl(e.getFileUrl());
        dto.setFileSize(e.getFileSize());
        dto.setContentType(e.getContentType());
        dto.setUploadedAt(e.getUploadedAt());
        dto.setJobRole(e.getJobRole());
        dto.setDescription(e.getDescription());
        return dto;
    }
}
