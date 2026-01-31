package com.school.sms.student.dto;

import com.school.sms.common.enums.BloodGroup;
import com.school.sms.common.enums.Gender;
import com.school.sms.common.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for student response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private Long userId;
    private String username;
    private String admissionNumber;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Integer age;
    private Gender gender;
    private BloodGroup bloodGroup;
    private String currentClass;
    private String section;
    private LocalDate admissionDate;
    private StudentStatus status;

    // Personal Information
    private String nationality;
    private String religion;
    private String caste;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    // Parent/Guardian Information
    private String fatherName;
    private String fatherOccupation;
    private String fatherPhone;
    private String fatherEmail;
    private String motherName;
    private String motherOccupation;
    private String motherPhone;
    private String motherEmail;
    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;
    private String guardianEmail;

    // Previous School Information
    private String previousSchool;
    private String previousClass;
    private String previousPercentage;

    // Medical Information
    private String medicalConditions;
    private String allergies;

    // Additional Information
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
