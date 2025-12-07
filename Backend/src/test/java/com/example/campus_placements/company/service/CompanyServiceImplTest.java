package com.example.campus_placements.company.service;

import com.example.campus_placements.company.dto.CompanyCreateRequest;
import com.example.campus_placements.company.dto.CompanySearchCriteria;
import com.example.campus_placements.company.dto.CompanyUpdateRequest;
import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.repository.CompanyRepository;
import com.example.campus_placements.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository repo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CompanyServiceImpl service;

    @Test
    void searchCompaniesTest() {
        CompanySearchCriteria criteria = new CompanySearchCriteria();
        criteria.setKeyword("java");
        criteria.setCategory("IT");
        criteria.setLocation("Chicago");

        Pageable pageable = Pageable.unpaged();

        Page<Company> expectedPage =
                new PageImpl<>(List.of(new Company()));
        when(repo.search(any(), any(), any(), any()))
                .thenReturn(expectedPage);

        Page<Company> result = service.searchCompanies(criteria, pageable);

        assertSame(expectedPage, result);

        ArgumentCaptor<String> kwCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> catCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> locCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> pageCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(repo).search(
                kwCaptor.capture(),
                catCaptor.capture(),
                locCaptor.capture(),
                pageCaptor.capture()
        );

        assertEquals("java", kwCaptor.getValue());
        assertEquals("IT", catCaptor.getValue());
        assertEquals("Chicago", locCaptor.getValue());
        org.junit.jupiter.api.Assertions.assertSame(pageable, pageCaptor.getValue());
    }

    @Test
    void addCompanyTest() {
        CompanyCreateRequest req = new CompanyCreateRequest();
        req.setName("Test Company");
        req.setDescription("Great place to work");
        req.setLocation("Chicago");
        req.setWebsite("https://example.com");
        req.setCategory("IT");

        when(repo.save(any(Company.class))).thenAnswer(inv -> {
            Company c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });


        Company result = service.createCompany(req);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Test Company", result.getName());
        assertEquals("Great place to work", result.getDescription());
        assertEquals("Chicago", result.getLocation());
        assertEquals("https://example.com", result.getWebsite());
        assertEquals("IT", result.getCategory());

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(repo).save(captor.capture());
        Company saved = captor.getValue();

        assertEquals("Test Company", saved.getName());
        assertEquals("Great place to work", saved.getDescription());
        assertEquals("Chicago", saved.getLocation());
        assertEquals("https://example.com", saved.getWebsite());
        assertEquals("IT", saved.getCategory());

    }

    @Test
    void updateCompanyTest() {
        Long id = 1L;

        Company existing = new Company();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setDescription("Old desc");
        existing.setLocation("Old City");
        existing.setWebsite("https://old.example.com");
        existing.setCategory("OLD_CAT");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        CompanyUpdateRequest req = new CompanyUpdateRequest();
        req.setId(id);
        req.setName("New Name");
        req.setDescription("New description");
        req.setLocation("New City");
        req.setWebsite("https://new.example.com");
        req.setCategory("IT");

        Company result = service.updateCompany(req);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("New Name", result.getName());
        assertEquals("New description", result.getDescription());
        assertEquals("New City", result.getLocation());
        assertEquals("https://new.example.com", result.getWebsite());
        assertEquals("IT", result.getCategory());

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(repo).save(captor.capture());
        Company saved = captor.getValue();

        assertEquals(id, saved.getId());
        assertEquals("New Name", saved.getName());
        assertEquals("New description", saved.getDescription());
        assertEquals("New City", saved.getLocation());
        assertEquals("https://new.example.com", saved.getWebsite());
        assertEquals("IT", saved.getCategory());
    }

    @Test
    void sendNotificationTest() {
        CompanyCreateRequest req = new CompanyCreateRequest();
        req.setName("Google");

        when(repo.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Company result = service.createCompany(req);

        ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
        verify(notificationService).notifyAllStudentsCompanyCreated(companyCaptor.capture());

        Company notified = companyCaptor.getValue();

        assertEquals("Google", notified.getName());

        assertEquals("Google", result.getName());
    }
}

