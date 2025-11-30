package com.example.campus_placements.practiceTest.controller;
import com.example.campus_placements.practiceTest.model.PracticeTest;
import org.springframework.http.*;
import com.example.campus_placements.practiceTest.dto.PracticeTestResponse;
import com.example.campus_placements.practiceTest.service.PracticeTestService;
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
            @RequestParam(value = "jobRole", required = false) String jobRole,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.upload(companyId, title, jobRole, description, file);
    }

    @DeleteMapping("/{practiceTestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long companyId,
                       @PathVariable Long practiceTestId) {
        service.delete(companyId, practiceTestId);
    }

    @GetMapping("/{practiceTestId}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable Long companyId,
                                          @PathVariable Long practiceTestId) {

        PracticeTest pt = service.getPracticeTest(companyId, practiceTestId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                pt.getContentType() != null ? pt.getContentType() : "application/pdf"));
        headers.setContentLength(pt.getFileSize());
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename(pt.getFileName() != null ? pt.getFileName() : "practice-test.pdf")
                        .build()
        );

        return new ResponseEntity<>(pt.getData(), headers, HttpStatus.OK);
    }
}

