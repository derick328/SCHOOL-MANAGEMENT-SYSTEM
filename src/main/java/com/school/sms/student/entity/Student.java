package com.school.sms.student.entity;

import com.school.sms.auth.entity.User;
import com.school.sms.common.entity.BaseEntity;
import com.school.sms.common.enums.BloodGroup;
import com.school.sms.common.enums.Gender;
import com.school.sms.common.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Student Entity
 * Extended profile information for students
 * Links to User entity for authentication
 */
@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_admission_number", columnList = "admissionNumber"),
        @Index(name = "idx_student_roll_number", columnList = "rollNumber"),
        @Index(name = "idx_student_class", columnList = "currentClass"),
        @Index(name = "idx_student_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank(message = "Admission number is required")
    @Column(unique = true, nullable = false, length = 50)
    private String admissionNumber;

    @Column(unique = true, length = 50)
    private String rollNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private BloodGroup bloodGroup;

    @NotBlank(message = "Current class is required")
    @Column(nullable = false, length = 50)
    private String currentClass;

    @Column(length = 20)
    private String section;

    @NotNull(message = "Admission date is required")
    @Column(nullable = false)
    private LocalDate admissionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;

    // Personal Information
    @Column(length = 100)
    private String nationality;

    @Column(length = 100)
    private String religion;

    @Column(length = 100)
    private String caste;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Pattern(regexp = "^[0-9]{5,10}$", message = "Invalid postal code")
    @Column(length = 10)
    private String postalCode;

    @Column(length = 50)
    private String country;

    // Parent/Guardian Information
    @NotBlank(message = "Father's name is required")
    @Column(nullable = false, length = 100)
    private String fatherName;

    @Column(length = 100)
    private String fatherOccupation;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    @Column(length = 15)
    private String fatherPhone;

    @Email(message = "Invalid email format")
    @Column(length = 100)
    private String fatherEmail;

    @Column(length = 100)
    private String motherName;

    @Column(length = 100)
    private String motherOccupation;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    @Column(length = 15)
    private String motherPhone;

    @Email(message = "Invalid email format")
    @Column(length = 100)
    private String motherEmail;

    @Column(length = 100)
    private String guardianName;

    @Column(length = 100)
    private String guardianRelation;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    @Column(length = 15)
    private String guardianPhone;

    @Email(message = "Invalid email format")
    @Column(length = 100)
    private String guardianEmail;

    // Previous School Information
    @Column(length = 200)
    private String previousSchool;

    @Column(length = 50)
    private String previousClass;

    @Column(length = 20)
    private String previousPercentage;

    // Medical Information
    @Column(columnDefinition = "TEXT")
    private String medicalConditions;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    // Documents
    @Column
    private String birthCertificateUrl;

    @Column
    private String transferCertificateUrl;

    @Column
    private String previousMarksheetUrl;

    // Additional Information
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Get student's age
     */
    @Transient
    public int getAge() {
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Get student's full name from user
     */
    @Transient
    public String getFullName() {
        return user != null ? user.getFullName() : "";
    }
}
