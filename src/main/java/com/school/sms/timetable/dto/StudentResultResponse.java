package com.school.sms.timetable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for student result responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResultResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private String admissionNumber;
    private String className;
    private String section;
    private String subject;
    private String examType;
    private String academicYear;
    private String term;
    private Double marksObtained;
    private Double totalMarks;
    private Double percentage;
    private String grade;
    private String remarks;
    private Boolean isPublished;
    private String createdAt;
    private String updatedAt;
}
