package com.school.sms.teacher.dto;

import com.school.sms.teacher.entity.Teacher;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating or updating a teacher
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequest {

    private Long userId; // Link to existing user or create new one

    // Employee ID will be auto-generated

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9+()-\\s]*$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Teacher.Gender gender;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 100, message = "Qualification must not exceed 100 characters")
    private String qualification;

    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    @Min(value = 0, message = "Experience years must be positive")
    @Max(value = 50, message = "Experience years must not exceed 50")
    private Integer experienceYears;

    private LocalDate joiningDate;

    @PositiveOrZero(message = "Salary must be positive")
    private Double salary;

    private Teacher.EmploymentStatus employmentStatus;

    @Size(max = 500, message = "Subjects must not exceed 500 characters")
    private String subjects;

    // For creating new user account
    private String username;
    private String password;
}
