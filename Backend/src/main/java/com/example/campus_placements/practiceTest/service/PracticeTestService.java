package com.example.campus_placements.practiceTest.service;

import com.example.campus_placements.practiceTest.dto.PracticeTestResponse;
import com.example.campus_placements.practiceTest.model.PracticeTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PracticeTestService {

    List<PracticeTestResponse> listForCompany(Long companyId);

    PracticeTestResponse upload(Long companyId, String title, String jobRole,
                                String description, MultipartFile file) throws IOException;

    void delete(Long companyId, Long practiceTestId);
    PracticeTest getPracticeTest(Long companyId, Long practiceTestId);
}
