package com.school.sms.timetable.service;

import com.school.sms.common.exception.ResourceNotFoundException;
import com.school.sms.student.entity.Student;
import com.school.sms.student.repository.StudentRepository;
import com.school.sms.timetable.dto.StudentResultRequest;
import com.school.sms.timetable.dto.StudentResultResponse;
import com.school.sms.timetable.entity.StudentResult;
import com.school.sms.timetable.repository.StudentResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Student Results management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentResultService {

    private final StudentResultRepository studentResultRepository;
    private final StudentRepository studentRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public StudentResultResponse createResult(StudentResultRequest request) {
        log.info("Creating result for student id: {}, subject: {}", request.getStudentId(), request.getSubject());

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        StudentResult result = StudentResult.builder()
                .student(student)
                .subject(request.getSubject())
                .examType(request.getExamType())
                .academicYear(request.getAcademicYear())
                .term(request.getTerm())
                .marksObtained(request.getMarksObtained())
                .totalMarks(request.getTotalMarks())
                .remarks(request.getRemarks())
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : false)
                .build();

        result = studentResultRepository.save(result);
        log.info("Result created successfully with id: {}", result.getId());

        return mapToResponse(result);
    }

    @Transactional
    public StudentResultResponse updateResult(Long id, StudentResultRequest request) {
        log.info("Updating result with id: {}", id);

        StudentResult result = studentResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        result.setStudent(student);
        result.setSubject(request.getSubject());
        result.setExamType(request.getExamType());
        result.setAcademicYear(request.getAcademicYear());
        result.setTerm(request.getTerm());
        result.setMarksObtained(request.getMarksObtained());
        result.setTotalMarks(request.getTotalMarks());
        result.setRemarks(request.getRemarks());
        if (request.getIsPublished() != null) {
            result.setIsPublished(request.getIsPublished());
        }

        result = studentResultRepository.save(result);
        log.info("Result updated successfully");

        return mapToResponse(result);
    }

    @Transactional(readOnly = true)
    public StudentResultResponse getResult(Long id) {
        StudentResult result = studentResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));
        return mapToResponse(result);
    }

    @Transactional(readOnly = true)
    public List<StudentResultResponse> getResultsByStudent(Long studentId) {
        return studentResultRepository.findByStudent_Id(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResultResponse> getResultsByStudentAndYear(Long studentId, String academicYear) {
        return studentResultRepository.findByStudent_IdAndAcademicYear(studentId, academicYear).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResultResponse> getResultsByClass(String className, String section) {
        return studentResultRepository.findByStudent_CurrentClassAndStudent_Section(className, section).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteResult(Long id) {
        StudentResult result = studentResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + id));
        studentResultRepository.delete(result);
        log.info("Result deleted successfully with id: {}", id);
    }

    private StudentResultResponse mapToResponse(StudentResult result) {
        return StudentResultResponse.builder()
                .id(result.getId())
                .studentId(result.getStudent().getId())
                .studentName(result.getStudent().getUser().getFullName())
                .admissionNumber(result.getStudent().getAdmissionNumber())
                .className(result.getStudent().getCurrentClass())
                .section(result.getStudent().getSection())
                .subject(result.getSubject())
                .examType(result.getExamType())
                .academicYear(result.getAcademicYear())
                .term(result.getTerm())
                .marksObtained(result.getMarksObtained())
                .totalMarks(result.getTotalMarks())
                .percentage(result.getPercentage())
                .grade(result.getGrade())
                .remarks(result.getRemarks())
                .isPublished(result.getIsPublished())
                .createdAt(result.getCreatedAt() != null ? result.getCreatedAt().format(FORMATTER) : null)
                .updatedAt(result.getUpdatedAt() != null ? result.getUpdatedAt().format(FORMATTER) : null)
                .build();
    }
}
