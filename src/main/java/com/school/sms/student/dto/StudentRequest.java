package com.school.sms.student.dto;

import com.school.sms.common.enums.BloodGroup;
import com.school.sms.common.enums.Gender;
import com.school.sms.common.enums.StudentStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating or updating a student
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    private Long userId; // Link to existing user or create new one

    // Admission number will be auto-generated if not provided
    private String admissionNumber;

    private String rollNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private BloodGroup bloodGroup;

    @NotBlank(message = "Current class is required")
    @Size(max = 50, message = "Class must not exceed 50 characters")
    private String currentClass;

    @Size(max = 20, message = "Section must not exceed 20 characters")
    private String section;

    @NotNull(message = "Admission date is required")
    private LocalDate admissionDate;

    private StudentStatus status;

    // Personal Information
    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    private String nationality;

    @Size(max = 100, message = "Religion must not exceed 100 characters")
    private String religion;

    @Size(max = 100, message = "Caste must not exceed 100 characters")
    private String caste;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Pattern(regexp = "^[0-9]{5,10}$", message = "Invalid postal code")
    private String postalCode;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    // Parent/Guardian Information
    @NotBlank(message = "Father's name is required")
    @Size(max = 100, message = "Father's name must not exceed 100 characters")
    private String fatherName;

    @Size(max = 100, message = "Father's occupation must not exceed 100 characters")
    private String fatherOccupation;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String fatherPhone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String fatherEmail;

    @Size(max = 100, message = "Mother's name must not exceed 100 characters")
    private String motherName;

    @Size(max = 100, message = "Mother's occupation must not exceed 100 characters")
    private String motherOccupation;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String motherPhone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String motherEmail;

    @Size(max = 100, message = "Guardian name must not exceed 100 characters")
    private String guardianName;

    @Size(max = 100, message = "Guardian relation must not exceed 100 characters")
    private String guardianRelation;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String guardianPhone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String guardianEmail;

    // Previous School Information
    @Size(max = 200, message = "Previous school must not exceed 200 characters")
    private String previousSchool;

    @Size(max = 50, message = "Previous class must not exceed 50 characters")
    private String previousClass;

    @Size(max = 20, message = "Previous percentage must not exceed 20 characters")
    private String previousPercentage;

    // Medical Information
    @Size(max = 1000, message = "Medical conditions must not exceed 1000 characters")
    private String medicalConditions;

    @Size(max = 1000, message = "Allergies must not exceed 1000 characters")
    private String allergies;

    // Additional Information
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    // For creating new user account
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

    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    private String username;
    private String password;
}
