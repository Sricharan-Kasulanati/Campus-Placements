package com.example.campus_placements.admin.controller;

import com.example.campus_placements.admin.dto.AdminStudentResponse;
import com.example.campus_placements.admin.service.AdminService;
import com.example.campus_placements.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getStudentInfoTest() throws Exception {
        Long studentId = 1L;

        AdminStudentResponse.RegisteredCompanyDto rc = new AdminStudentResponse.RegisteredCompanyDto();
        rc.setId(10L);
        rc.setName("Test Company");

        AdminStudentResponse response = new AdminStudentResponse();
        response.setId(studentId);
        response.setFullName("Test Student");
        response.setEmail("student@example.com");
        response.setRegisteredCompanies(List.of(rc));

        when(adminService.getStudent(studentId)).thenReturn(response);

        mockMvc.perform(get("/api/admin/students/{studentId}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Test Student"))
                .andExpect(jsonPath("$.email").value("student@example.com"))
                .andExpect(jsonPath("$.registeredCompanies[0].id").value(10))
                .andExpect(jsonPath("$.registeredCompanies[0].name").value("Test Company"));
    }
}

