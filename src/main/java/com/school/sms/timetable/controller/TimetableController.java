package com.school.sms.timetable.controller;

import com.school.sms.common.dto.ApiResponse;
import com.school.sms.timetable.dto.ConflictCheckResponse;
import com.school.sms.timetable.dto.TimetableRequest;
import com.school.sms.timetable.dto.TimetableResponse;
import com.school.sms.timetable.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

/**
 * REST Controller for Timetable management
 */
@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACADEMIC_TEACHER')")
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping
    public ResponseEntity<ApiResponse<TimetableResponse>> createTimetable(
            @Valid @RequestBody TimetableRequest request) {
        TimetableResponse timetable = timetableService.createTimetable(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Timetable created successfully", timetable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TimetableResponse>> updateTimetable(
            @PathVariable Long id,
            @Valid @RequestBody TimetableRequest request) {
        TimetableResponse timetable = timetableService.updateTimetable(id, request);
        return ResponseEntity.ok(ApiResponse.success("Timetable updated successfully", timetable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'TEACHER', 'ACADEMIC_TEACHER', 'DISCIPLINE_TEACHER')")
    public ResponseEntity<ApiResponse<TimetableResponse>> getTimetable(@PathVariable Long id) {
        TimetableResponse timetable = timetableService.getTimetable(id);
        return ResponseEntity.ok(ApiResponse.success("Timetable retrieved successfully", timetable));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'TEACHER', 'ACADEMIC_TEACHER', 'DISCIPLINE_TEACHER')")
    public ResponseEntity<ApiResponse<List<TimetableResponse>>> getAllTimetables(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) DayOfWeek dayOfWeek) {

        List<TimetableResponse> timetables;

        if (className != null) {
            timetables = timetableService.getTimetableByClass(className, section);
        } else if (teacherId != null) {
            timetables = timetableService.getTimetableByTeacher(teacherId);
        } else if (dayOfWeek != null) {
            timetables = timetableService.getTimetableByDay(dayOfWeek);
        } else {
            timetables = timetableService.getAllTimetables();
        }

        return ResponseEntity.ok(ApiResponse.success(
                "Timetables retrieved successfully (" + timetables.size() + " found)", timetables));
    }

    @PostMapping("/check-conflicts")
    public ResponseEntity<ApiResponse<ConflictCheckResponse>> checkConflicts(
            @Valid @RequestBody TimetableRequest request,
            @RequestParam(required = false) Long excludeId) {
        ConflictCheckResponse response = timetableService.checkConflicts(request, excludeId);
        return ResponseEntity.ok(ApiResponse.success("Conflict check completed", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
        return ResponseEntity.ok(ApiResponse.success("Timetable deleted successfully", null));
    }
}
