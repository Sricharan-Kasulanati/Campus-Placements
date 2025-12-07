package com.example.campus_placements.practiceTest.service;

import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.notification.service.NotificationService;
import com.example.campus_placements.practiceTest.dto.PracticeTestResponse;
import com.example.campus_placements.practiceTest.model.PracticeTest;
import com.example.campus_placements.practiceTest.repository.PracticeTestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PracticeTestServiceImplTest {

    @Mock
    private PracticeTestRepository repository;

    @Mock
    private CompanyRepository companies;

    @InjectMocks
    private PracticeTestServiceImpl service;

    @Mock
    private NotificationService notificationService;

    @Test
    void addPracticeTestTest() throws IOException {
        Long companyId = 1L;

        Company company = new Company();
        company.setId(companyId);
        company.setName("Test Company");
        when(companies.findById(companyId)).thenReturn(Optional.of(company));

        byte[] content = "dummy pdf content".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                content
        );

        when(repository.save(any(PracticeTest.class))).thenAnswer(inv -> {
            PracticeTest pt = inv.getArgument(0);
            pt.setId(99L);
            return pt;
        });

        PracticeTestResponse res = service.upload(
                companyId,
                "Sample Test",
                "SDE",
                "Some description",
                file
        );

        assertNotNull(res);
        assertEquals(99L, res.getId());
        assertEquals(companyId, res.getCompanyId());
        assertEquals("Sample Test", res.getTitle());
        assertEquals("SDE", res.getJobRole());
        assertEquals("Some description", res.getDescription());
        assertEquals(file.getSize(), res.getFileSize());
        assertEquals(file.getContentType(), res.getContentType());
        assertNotNull(res.getUploadedAt());
        assertNotNull(res.getFileUrl());

        ArgumentCaptor<PracticeTest> captor = ArgumentCaptor.forClass(PracticeTest.class);
        verify(repository).save(captor.capture());
        PracticeTest saved = captor.getValue();

        assertEquals(company, saved.getCompany());
        assertEquals("Sample Test", saved.getTitle());
        assertEquals("SDE", saved.getJobRole());
        assertEquals("Some description", saved.getDescription());
        assertEquals(file.getSize(), saved.getFileSize());
    }

    @Test
    void downloadPracticeTestTest() {
        Long companyId = 1L;
        Long testId = 5L;

        Company company = new Company();
        company.setId(companyId);
        company.setName("Test Company");

        PracticeTest pt = new PracticeTest();
        pt.setId(testId);
        pt.setCompany(company);
        pt.setTitle("Sample Prep");
        pt.setJobRole("SDE");
        pt.setDescription("Some description");
        pt.setFileName("prep.pdf");
        pt.setContentType("application/pdf");
        pt.setData("dummy content".getBytes(StandardCharsets.UTF_8));

        when(repository.findByIdAndCompanyId(testId, companyId))
                .thenReturn(Optional.of(pt));

        PracticeTest result = service.getPracticeTest(companyId, testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertNotNull(result.getCompany());
        assertEquals(companyId, result.getCompany().getId());
        assertEquals("Sample Prep", result.getTitle());
        assertEquals("SDE", result.getJobRole());
        assertEquals("prep.pdf", result.getFileName());
        assertEquals("application/pdf", result.getContentType());
        assertArrayEquals("dummy content".getBytes(StandardCharsets.UTF_8), result.getData());

        verify(repository).findByIdAndCompanyId(testId, companyId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(notificationService);
    }

    @Test
    void downloadPracticeTest_throwsWhenNotFound() {
        Long companyId = 1L;
        Long testId = 999L;

        when(repository.findByIdAndCompanyId(testId, companyId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.getPracticeTest(companyId, testId));

        verify(repository).findByIdAndCompanyId(testId, companyId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(notificationService);
    }

    @Test
    void browsePracticeTests() {
        Long companyId = 1L;

        Company company = new Company();
        company.setId(companyId);
        company.setName("Test Company");

        PracticeTest pt1 = new PracticeTest();
        pt1.setId(10L);
        pt1.setCompany(company);
        pt1.setTitle("DSA Prep");
        pt1.setJobRole("SDE");
        pt1.setDescription("Data structures & algorithms questions");
        pt1.setContentType("application/pdf");
        pt1.setFileSize(12345L);
        pt1.setUploadedAt(Instant.parse("2025-01-01T10:00:00Z"));

        PracticeTest pt2 = new PracticeTest();
        pt2.setId(20L);
        pt2.setCompany(company);
        pt2.setTitle("SQL Practice");
        pt2.setJobRole("Data Engineer");
        pt2.setDescription("SQL queries and solutions");
        pt2.setContentType("application/pdf");
        pt2.setFileSize(67890L);
        pt2.setUploadedAt(Instant.parse("2025-01-02T11:00:00Z"));

        when(repository.findByCompanyId(companyId))
                .thenReturn(List.of(pt1, pt2));
        List<PracticeTestResponse> result = service.listForCompany(companyId);
        assertNotNull(result);
        assertEquals(2, result.size());

        PracticeTestResponse r1 = result.get(0);
        assertEquals(10L, r1.getId());
        assertEquals(companyId, r1.getCompanyId());
        assertEquals("DSA Prep", r1.getTitle());
        assertEquals("SDE", r1.getJobRole());
        assertEquals("Data structures & algorithms questions", r1.getDescription());
        assertEquals(12345L, r1.getFileSize());
        assertEquals("application/pdf", r1.getContentType());
        assertEquals(Instant.parse("2025-01-01T10:00:00Z"), r1.getUploadedAt());
        assertEquals("/api/companies/1/practice-tests/10/file", r1.getFileUrl());

        PracticeTestResponse r2 = result.get(1);
        assertEquals(20L, r2.getId());
        assertEquals(companyId, r2.getCompanyId());
        assertEquals("SQL Practice", r2.getTitle());
        assertEquals("Data Engineer", r2.getJobRole());
        assertEquals("SQL queries and solutions", r2.getDescription());
        assertEquals(67890L, r2.getFileSize());
        assertEquals("application/pdf", r2.getContentType());
        assertEquals(Instant.parse("2025-01-02T11:00:00Z"), r2.getUploadedAt());
        assertEquals("/api/companies/1/practice-tests/20/file", r2.getFileUrl());

        verify(repository).findByCompanyId(companyId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(notificationService);
    }

    @Test
    void browsePracticeTests_whenNoTestsExist() {
        Long companyId = 2L;

        when(repository.findByCompanyId(companyId))
                .thenReturn(Collections.emptyList());

        List<PracticeTestResponse> result = service.listForCompany(companyId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findByCompanyId(companyId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(notificationService);
    }
}

