package com.school.sms.teacher.service;

import com.school.sms.common.exception.BadRequestException;
import com.school.sms.common.exception.ResourceNotFoundException;
import com.school.sms.teacher.dto.TeacherRequest;
import com.school.sms.teacher.dto.TeacherResponse;
import com.school.sms.teacher.entity.Teacher;
import com.school.sms.teacher.repository.TeacherRepository;
import com.school.sms.common.enums.Role;
import com.school.sms.auth.entity.User;
import com.school.sms.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing teachers
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        log.info("Creating new teacher with employee ID: {}", request.getEmployeeId());

        // Check if employee ID already exists
        if (teacherRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new BadRequestException("Employee ID already exists");
        }

        // Check if email already exists
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Create or get user
        User user;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            // Create new user account
            if (request.getUsername() == null || request.getPassword() == null) {
                throw new BadRequestException("Username and password are required to create new teacher account");
            }

            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username already exists");
            }

            user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .role(Role.TEACHER)
                    .build();
            user = userRepository.save(user);
        }

        // Create teacher
        Teacher teacher = Teacher.builder()
                .user(user)
                .employeeId(request.getEmployeeId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .qualification(request.getQualification())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .joiningDate(request.getJoiningDate())
                .salary(request.getSalary())
                .employmentStatus(request.getEmploymentStatus() != null
                        ? request.getEmploymentStatus()
                        : Teacher.EmploymentStatus.ACTIVE)
                .subjects(request.getSubjects())
                .build();

        teacher = teacherRepository.save(teacher);
        log.info("Teacher created successfully with ID: {}", teacher.getId());

        return mapToResponse(teacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        log.info("Updating teacher with ID: {}", id);

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // Check if employee ID is being changed and if it already exists
        if (!teacher.getEmployeeId().equals(request.getEmployeeId())
                && teacherRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new BadRequestException("Employee ID already exists");
        }

        // Check if email is being changed and if it already exists
        if (!teacher.getEmail().equals(request.getEmail())
                && teacherRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Update teacher fields
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setGender(request.getGender());
        teacher.setAddress(request.getAddress());
        teacher.setQualification(request.getQualification());
        teacher.setSpecialization(request.getSpecialization());
        teacher.setExperienceYears(request.getExperienceYears());
        teacher.setJoiningDate(request.getJoiningDate());
        teacher.setSalary(request.getSalary());
        teacher.setEmploymentStatus(request.getEmploymentStatus());
        teacher.setSubjects(request.getSubjects());

        teacher = teacherRepository.save(teacher);
        log.info("Teacher updated successfully");

        return mapToResponse(teacher);
    }

    @Transactional(readOnly = true)
    public TeacherResponse getTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return mapToResponse(teacher);
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> searchTeachers(String search) {
        return teacherRepository.searchTeachers(search).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getTeachersByStatus(Teacher.EmploymentStatus status) {
        return teacherRepository.findByEmploymentStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTeacher(Long id) {
        log.info("Deleting teacher with ID: {}", id);

        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher not found");
        }

        teacherRepository.deleteById(id);
        log.info("Teacher deleted successfully");
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .userId(teacher.getUser().getId())
                .username(teacher.getUser().getUsername())
                .employeeId(teacher.getEmployeeId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .fullName(teacher.getFullName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .dateOfBirth(teacher.getDateOfBirth())
                .gender(teacher.getGender())
                .address(teacher.getAddress())
                .qualification(teacher.getQualification())
                .specialization(teacher.getSpecialization())
                .experienceYears(teacher.getExperienceYears())
                .joiningDate(teacher.getJoiningDate())
                .salary(teacher.getSalary())
                .employmentStatus(teacher.getEmploymentStatus())
                .subjects(teacher.getSubjects())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }
}
