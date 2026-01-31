package com.school.sms.timetable.controller;

import com.school.sms.common.dto.ApiResponse;
import com.school.sms.timetable.dto.StudentResultRequest;
import com.school.sms.timetable.dto.StudentResultResponse;
import com.school.sms.timetable.service.StudentResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Student Results management
 */
@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'TEACHER', 'ACADEMIC_TEACHER')")
public class StudentResultController {

    private final StudentResultService studentResultService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResultResponse>> createResult(
            @Valid @RequestBody StudentResultRequest request) {
        StudentResultResponse result = studentResultService.createResult(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Result created successfully", result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResultResponse>> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody StudentResultRequest request) {
        StudentResultResponse result = studentResultService.updateResult(id, request);
        return ResponseEntity.ok(ApiResponse.success("Result updated successfully", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResultResponse>> getResult(@PathVariable Long id) {
        StudentResultResponse result = studentResultService.getResult(id);
        return ResponseEntity.ok(ApiResponse.success("Result retrieved successfully", result));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<StudentResultResponse>>> getResultsByStudent(
            @PathVariable Long studentId,
            @RequestParam(required = false) String academicYear) {

        List<StudentResultResponse> results;
        if (academicYear != null) {
            results = studentResultService.getResultsByStudentAndYear(studentId, academicYear);
        } else {
            results = studentResultService.getResultsByStudent(studentId);
        }

        return ResponseEntity.ok(ApiResponse.success(
                "Results retrieved successfully (" + results.size() + " found)", results));
    }

    @GetMapping("/class")
    public ResponseEntity<ApiResponse<List<StudentResultResponse>>> getResultsByClass(
            @RequestParam String className,
            @RequestParam(required = false) String section) {

        List<StudentResultResponse> results = studentResultService.getResultsByClass(className, section);
        return ResponseEntity.ok(ApiResponse.success(
                "Results retrieved successfully (" + results.size() + " found)", results));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResult(@PathVariable Long id) {
        studentResultService.deleteResult(id);
        return ResponseEntity.ok(ApiResponse.success("Result deleted successfully", null));
    }
}
