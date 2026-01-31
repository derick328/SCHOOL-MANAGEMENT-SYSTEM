package com.school.sms.timetable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for creating/updating timetable entries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableRequest {

    @NotBlank(message = "Class name is required")
    private String className;

    private String section;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private String room;

    private String notes;

    private Boolean isActive;
}
