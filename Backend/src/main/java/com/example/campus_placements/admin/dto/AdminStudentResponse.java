package com.example.campus_placements.admin.dto;
import java.util.List;

public class AdminStudentResponse {

    private Long id;
    private String fullName;
    private String email;
    private List<RegisteredCompanyDto> registeredCompanies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RegisteredCompanyDto> getRegisteredCompanies() {
        return registeredCompanies;
    }

    public void setRegisteredCompanies(List<RegisteredCompanyDto> registeredCompanies) {
        this.registeredCompanies = registeredCompanies;
    }

    public static class RegisteredCompanyDto {

        private Long id;
        private String name;

        public RegisteredCompanyDto() {
        }

        public RegisteredCompanyDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

