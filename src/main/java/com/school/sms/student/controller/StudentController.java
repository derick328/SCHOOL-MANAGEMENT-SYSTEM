package com.school.sms.student.controller;

import com.school.sms.common.dto.ApiResponse;
import com.school.sms.common.enums.StudentStatus;
import com.school.sms.student.dto.StudentRequest;
import com.school.sms.student.dto.StudentResponse;
import com.school.sms.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Student management
 */
@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @Valid @RequestBody StudentRequest request) {
        StudentResponse student = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        StudentResponse student = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudent(@PathVariable Long id) {
        StudentResponse student = studentService.getStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) StudentStatus status,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String section) {

        List<StudentResponse> students;

        if (search != null && !search.trim().isEmpty()) {
            students = studentService.searchStudents(search);
        } else if (status != null) {
            students = studentService.getStudentsByStatus(status);
        } else if (className != null && section != null) {
            students = studentService.getStudentsByClassAndSection(className, section);
        } else if (className != null) {
            students = studentService.getStudentsByClass(className);
        } else {
            students = studentService.getAllStudents();
        }

        return ResponseEntity.ok(ApiResponse.success(
                "Students retrieved successfully (" + students.size() + " found)", students));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }
}
