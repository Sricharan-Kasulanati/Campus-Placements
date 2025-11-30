package com.example.campus_placements.practiceTest.controller;

import com.example.campus_placements.practiceTest.dto.PracticeTestResponse;
import com.example.campus_placements.practiceTest.service.PracticeTestService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/companies/{companyId}/practice-tests")
public class PracticeTestController {

    private final PracticeTestService service;

    public PracticeTestController(PracticeTestService service) {
        this.service = service;
    }

    @GetMapping
    public List<PracticeTestResponse> list(@PathVariable Long companyId) {
        return service.listForCompany(companyId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public PracticeTestResponse upload(
            @PathVariable Long companyId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.upload(companyId, title, file);
    }

    @DeleteMapping("/{practiceTestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(
            @PathVariable Long companyId,
            @PathVariable Long practiceTestId) {
        service.delete(companyId, practiceTestId);
    }
}

