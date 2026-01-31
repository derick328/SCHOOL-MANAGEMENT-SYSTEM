package com.school.sms.timetable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for timetable responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableResponse {

    private Long id;
    private String className;
    private String section;
    private String subject;
    private Long teacherId;
    private String teacherName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private String notes;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
