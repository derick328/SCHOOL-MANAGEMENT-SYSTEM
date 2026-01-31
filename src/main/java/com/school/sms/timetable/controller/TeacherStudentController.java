package com.school.sms.timetable.controller;

import com.school.sms.common.dto.ApiResponse;
import com.school.sms.common.enums.StudentStatus;
import com.school.sms.student.dto.StudentResponse;
import com.school.sms.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Teachers to view Students
 */
@RestController
@RequestMapping("/api/teacher/students")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TEACHER', 'ACADEMIC_TEACHER', 'DISCIPLINE_TEACHER')")
public class TeacherStudentController {

    private final StudentService studentService;

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
}
