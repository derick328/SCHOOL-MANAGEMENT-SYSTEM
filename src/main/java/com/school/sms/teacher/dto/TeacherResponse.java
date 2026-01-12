package com.school.sms.teacher.dto;

import com.school.sms.teacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for teacher response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponse {

    private Long id;
    private Long userId;
    private String username;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Teacher.Gender gender;
    private String address;
    private String qualification;
    private String specialization;
    private Integer experienceYears;
    private LocalDate joiningDate;
    private Double salary;
    private Teacher.EmploymentStatus employmentStatus;
    private String subjects;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
