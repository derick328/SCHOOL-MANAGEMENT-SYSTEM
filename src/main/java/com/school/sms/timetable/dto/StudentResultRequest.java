package com.school.sms.timetable.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating/updating student results
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResultRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Exam type is required")
    private String examType;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    private String term;

    @NotNull(message = "Marks obtained is required")
    @Min(value = 0, message = "Marks cannot be negative")
    private Double marksObtained;

    @NotNull(message = "Total marks is required")
    @Min(value = 1, message = "Total marks must be positive")
    private Double totalMarks;

    private String remarks;

    private Boolean isPublished;
}
