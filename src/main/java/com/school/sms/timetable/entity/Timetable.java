package com.school.sms.timetable.entity;

import com.school.sms.common.entity.BaseEntity;
import com.school.sms.teacher.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Timetable Entity
 * Represents a single class schedule slot
 */
@Entity
@Table(name = "timetables", indexes = {
        @Index(name = "idx_timetable_class", columnList = "className, section"),
        @Index(name = "idx_timetable_teacher", columnList = "teacher_id"),
        @Index(name = "idx_timetable_day_time", columnList = "dayOfWeek, startTime")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timetable extends BaseEntity {

    @NotBlank(message = "Class name is required")
    @Column(nullable = false, length = 50)
    private String className;

    @Column(length = 20)
    private String section;

    @NotBlank(message = "Subject is required")
    @Column(nullable = false, length = 100)
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @NotNull(message = "Teacher is required")
    private Teacher teacher;

    @NotNull(message = "Day of week is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(nullable = false)
    private LocalTime endTime;

    @Column(length = 50)
    private String room;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Check if this timetable slot overlaps with another
     */
    @Transient
    public boolean overlapsWith(Timetable other) {
        if (!this.dayOfWeek.equals(other.getDayOfWeek())) {
            return false;
        }

        // Check time overlap: slots overlap if one starts before the other ends
        return this.startTime.isBefore(other.getEndTime()) &&
                this.endTime.isAfter(other.getStartTime());
    }

    /**
     * Get full class name with section
     */
    @Transient
    public String getFullClassName() {
        return section != null ? className + " - " + section : className;
    }
}
