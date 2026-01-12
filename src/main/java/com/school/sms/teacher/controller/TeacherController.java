package com.school.sms.teacher.controller;

import com.school.sms.common.ApiResponse;
import com.school.sms.teacher.dto.TeacherRequest;
import com.school.sms.teacher.dto.TeacherResponse;
import com.school.sms.teacher.entity.Teacher;
import com.school.sms.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Teacher management
 */
@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(
            @Valid @RequestBody TeacherRequest request) {
        TeacherResponse teacher = teacherService.createTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(teacher, "Teacher created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequest request) {
        TeacherResponse teacher = teacherService.updateTeacher(id, request);
        return ResponseEntity.ok(ApiResponse.success(teacher, "Teacher updated successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getTeacher(@PathVariable Long id) {
        TeacherResponse teacher = teacherService.getTeacher(id);
        return ResponseEntity.ok(ApiResponse.success(teacher, "Teacher retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAllTeachers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Teacher.EmploymentStatus status) {

        List<TeacherResponse> teachers;

        if (search != null && !search.trim().isEmpty()) {
            teachers = teacherService.searchTeachers(search);
        } else if (status != null) {
            teachers = teacherService.getTeachersByStatus(status);
        } else {
            teachers = teacherService.getAllTeachers();
        }

        return ResponseEntity.ok(ApiResponse.success(teachers,
                "Teachers retrieved successfully (" + teachers.size() + " found)"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Teacher deleted successfully"));
    }
}
