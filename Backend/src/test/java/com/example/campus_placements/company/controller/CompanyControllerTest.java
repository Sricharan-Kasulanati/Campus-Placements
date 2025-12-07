package com.example.campus_placements.company.controller;


import com.example.campus_placements.company.model.Company;
import com.example.campus_placements.company.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @Test
    @WithMockUser(username = "student@example.com", roles = {"STUDENT"})
    void authenticatedUserGetCompaniesTest() throws Exception {
        Company c = new Company();
        c.setId(1L);
        c.setName("Test Company");
        c.setDescription("Great place to work");
        c.setLocation("Remote");
        c.setWebsite("https://example.com");
        c.setCategory("IT");

        Page<Company> page = new PageImpl<>(List.of(c));

        when(companyService.listCompanies(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Company"))
                .andExpect(jsonPath("$.content[0].location").value("Remote"));
    }
}


