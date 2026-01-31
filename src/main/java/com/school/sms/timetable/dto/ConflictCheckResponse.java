package com.school.sms.timetable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for conflict detection response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConflictCheckResponse {

    private Boolean hasConflict;
    private List<ConflictDetail> conflicts;
    private String message;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConflictDetail {
        private String type; // "TEACHER_CONFLICT", "ROOM_CONFLICT", "CLASS_CONFLICT"
        private String description;
        private TimetableResponse conflictingSlot;
    }
}
