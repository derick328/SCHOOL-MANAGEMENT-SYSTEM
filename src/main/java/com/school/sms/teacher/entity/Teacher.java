package com.school.sms.teacher.entity;

import com.school.sms.common.entity.BaseEntity;
import com.school.sms.auth.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

/**
 * Teacher Entity
 * Represents a teacher in the school management system
 */
@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "employee_id", unique = true, nullable = false, length = 20)
    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^[0-9+()-\\s]*$", message = "Invalid phone number format")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "employment_status", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;

    @Column(name = "subjects", length = 500)
    private String subjects; // Comma-separated list of subjects

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum EmploymentStatus {
        ACTIVE, ON_LEAVE, RESIGNED, TERMINATED
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
