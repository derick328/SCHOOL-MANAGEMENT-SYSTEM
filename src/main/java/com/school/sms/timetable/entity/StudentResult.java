package com.school.sms.timetable.entity;

import com.school.sms.common.entity.BaseEntity;
import com.school.sms.student.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Student Result Entity
 * Stores academic results for students
 */
@Entity
@Table(name = "student_results", indexes = {
        @Index(name = "idx_result_student", columnList = "student_id"),
        @Index(name = "idx_result_exam", columnList = "examType, academicYear")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required")
    private Student student;

    @NotBlank(message = "Subject is required")
    @Column(nullable = false, length = 100)
    private String subject;

    @NotBlank(message = "Exam type is required")
    @Column(nullable = false, length = 50)
    private String examType; // e.g., "Midterm", "Final", "Quiz", "Assignment"

    @NotBlank(message = "Academic year is required")
    @Column(nullable = false, length = 20)
    private String academicYear; // e.g., "2025-2026"

    @Column(length = 20)
    private String term; // e.g., "Term 1", "Term 2", "Semester 1"

    @NotNull(message = "Marks obtained is required")
    @Min(value = 0, message = "Marks cannot be negative")
    @Column(nullable = false)
    private Double marksObtained;

    @NotNull(message = "Total marks is required")
    @Min(value = 1, message = "Total marks must be positive")
    @Column(nullable = false)
    private Double totalMarks;

    @Column(length = 5)
    private String grade; // e.g., "A+", "A", "B+", etc.

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPublished = false;

    /**
     * Calculate percentage
     */
    @Transient
    public Double getPercentage() {
        if (totalMarks == null || totalMarks == 0) {
            return 0.0;
        }
        return (marksObtained / totalMarks) * 100;
    }

    /**
     * Auto-calculate grade based on percentage
     */
    @PrePersist
    @PreUpdate
    private void calculateGrade() {
        if (marksObtained != null && totalMarks != null) {
            double percentage = getPercentage();
            if (percentage >= 90) {
                grade = "A+";
            } else if (percentage >= 80) {
                grade = "A";
            } else if (percentage >= 70) {
                grade = "B+";
            } else if (percentage >= 60) {
                grade = "B";
            } else if (percentage >= 50) {
                grade = "C";
            } else if (percentage >= 40) {
                grade = "D";
            } else {
                grade = "F";
            }
        }
    }
}
