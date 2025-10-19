package com.example.campus_placements.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateRequest {
    @NotBlank
    @Size(max = 80)  private String firstName;
    @NotBlank @Size(max = 80)  private String lastName;
    @Email
    @NotBlank @Size(max = 160) private String email;

    public @NotBlank @Size(max = 80) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank @Size(max = 80) String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank @Size(max = 80) String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Size(max = 80) String lastName) {
        this.lastName = lastName;
    }

    public @Email @NotBlank @Size(max = 160) String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank @Size(max = 160) String email) {
        this.email = email;
    }
}
