package com.school.sms.student.service;

import com.school.sms.auth.entity.User;
import com.school.sms.auth.repository.UserRepository;
import com.school.sms.common.enums.AccountStatus;
import com.school.sms.common.enums.Role;
import com.school.sms.common.enums.StudentStatus;
import com.school.sms.common.exception.BadRequestException;
import com.school.sms.common.exception.ResourceNotFoundException;
import com.school.sms.student.dto.StudentRequest;
import com.school.sms.student.dto.StudentResponse;
import com.school.sms.student.entity.Student;
import com.school.sms.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing students
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        log.info("Creating new student");

        // Generate admission number if not provided
        String admissionNumber = request.getAdmissionNumber();
        if (admissionNumber == null || admissionNumber.isEmpty()) {
            admissionNumber = generateAdmissionNumber();
        } else if (studentRepository.existsByAdmissionNumber(admissionNumber)) {
            throw new BadRequestException("Admission number already exists: " + admissionNumber);
        }

        // Check if roll number exists
        if (request.getRollNumber() != null && !request.getRollNumber().isEmpty()) {
            if (studentRepository.existsByRollNumber(request.getRollNumber())) {
                throw new BadRequestException("Roll number already exists: " + request.getRollNumber());
            }
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        // Check if username exists
        String username = request.getUsername();
        if (username == null || username.isEmpty()) {
            // Generate username from first and last name
            username = generateUsername(request.getFirstName(), request.getLastName());
        }
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists: " + username);
        }

        // Create user account
        User user = User.builder()
                .username(username)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword() != null ? request.getPassword() : "student123"))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(Role.STUDENT)
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(true)
                .build();

        user = userRepository.save(user);
        log.info("User account created for student: {}", username);

        // Create student profile
        Student student = Student.builder()
                .user(user)
                .admissionNumber(admissionNumber)
                .rollNumber(request.getRollNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .currentClass(request.getCurrentClass())
                .section(request.getSection())
                .admissionDate(request.getAdmissionDate() != null ? request.getAdmissionDate() : LocalDate.now())
                .status(request.getStatus() != null ? request.getStatus() : StudentStatus.ACTIVE)
                .nationality(request.getNationality())
                .religion(request.getReligion())
                .caste(request.getCaste())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .fatherName(request.getFatherName())
                .fatherOccupation(request.getFatherOccupation())
                .fatherPhone(request.getFatherPhone())
                .fatherEmail(request.getFatherEmail())
                .motherName(request.getMotherName())
                .motherOccupation(request.getMotherOccupation())
                .motherPhone(request.getMotherPhone())
                .motherEmail(request.getMotherEmail())
                .guardianName(request.getGuardianName())
                .guardianRelation(request.getGuardianRelation())
                .guardianPhone(request.getGuardianPhone())
                .guardianEmail(request.getGuardianEmail())
                .previousSchool(request.getPreviousSchool())
                .previousClass(request.getPreviousClass())
                .previousPercentage(request.getPreviousPercentage())
                .medicalConditions(request.getMedicalConditions())
                .allergies(request.getAllergies())
                .notes(request.getNotes())
                .build();

        student = studentRepository.save(student);
        log.info("Student created successfully with ID: {} and admission number: {}", student.getId(), admissionNumber);

        return mapToResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        log.info("Updating student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        User user = student.getUser();

        // Update user information
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);

        // Update student information
        if (request.getRollNumber() != null && !request.getRollNumber().equals(student.getRollNumber())) {
            if (studentRepository.existsByRollNumber(request.getRollNumber())) {
                throw new BadRequestException("Roll number already exists: " + request.getRollNumber());
            }
            student.setRollNumber(request.getRollNumber());
        }

        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            student.setGender(request.getGender());
        }
        if (request.getBloodGroup() != null) {
            student.setBloodGroup(request.getBloodGroup());
        }
        if (request.getCurrentClass() != null) {
            student.setCurrentClass(request.getCurrentClass());
        }
        if (request.getSection() != null) {
            student.setSection(request.getSection());
        }
        if (request.getStatus() != null) {
            student.setStatus(request.getStatus());
        }

        // Update personal information
        student.setNationality(request.getNationality());
        student.setReligion(request.getReligion());
        student.setCaste(request.getCaste());
        student.setAddress(request.getAddress());
        student.setCity(request.getCity());
        student.setState(request.getState());
        student.setPostalCode(request.getPostalCode());
        student.setCountry(request.getCountry());

        // Update parent/guardian information
        student.setFatherName(request.getFatherName());
        student.setFatherOccupation(request.getFatherOccupation());
        student.setFatherPhone(request.getFatherPhone());
        student.setFatherEmail(request.getFatherEmail());
        student.setMotherName(request.getMotherName());
        student.setMotherOccupation(request.getMotherOccupation());
        student.setMotherPhone(request.getMotherPhone());
        student.setMotherEmail(request.getMotherEmail());
        student.setGuardianName(request.getGuardianName());
        student.setGuardianRelation(request.getGuardianRelation());
        student.setGuardianPhone(request.getGuardianPhone());
        student.setGuardianEmail(request.getGuardianEmail());

        // Update previous school information
        student.setPreviousSchool(request.getPreviousSchool());
        student.setPreviousClass(request.getPreviousClass());
        student.setPreviousPercentage(request.getPreviousPercentage());

        // Update medical information
        student.setMedicalConditions(request.getMedicalConditions());
        student.setAllergies(request.getAllergies());

        // Update additional information
        student.setNotes(request.getNotes());

        student = studentRepository.save(student);
        log.info("Student updated successfully with ID: {}", id);

        return mapToResponse(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return mapToResponse(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> searchStudents(String search) {
        return studentRepository.searchStudents(search).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByClass(String className) {
        return studentRepository.findByCurrentClass(className).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByClassAndSection(String className, String section) {
        return studentRepository.findByCurrentClassAndSection(className, section).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);

        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found");
        }

        studentRepository.deleteById(id);
        log.info("Student deleted successfully");
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .userId(student.getUser().getId())
                .username(student.getUser().getUsername())
                .admissionNumber(student.getAdmissionNumber())
                .rollNumber(student.getRollNumber())
                .firstName(student.getUser().getFirstName())
                .lastName(student.getUser().getLastName())
                .fullName(student.getFullName())
                .email(student.getUser().getEmail())
                .phone(student.getUser().getPhone())
                .dateOfBirth(student.getDateOfBirth())
                .age(student.getAge())
                .gender(student.getGender())
                .bloodGroup(student.getBloodGroup())
                .currentClass(student.getCurrentClass())
                .section(student.getSection())
                .admissionDate(student.getAdmissionDate())
                .status(student.getStatus())
                .nationality(student.getNationality())
                .religion(student.getReligion())
                .caste(student.getCaste())
                .address(student.getAddress())
                .city(student.getCity())
                .state(student.getState())
                .postalCode(student.getPostalCode())
                .country(student.getCountry())
                .fatherName(student.getFatherName())
                .fatherOccupation(student.getFatherOccupation())
                .fatherPhone(student.getFatherPhone())
                .fatherEmail(student.getFatherEmail())
                .motherName(student.getMotherName())
                .motherOccupation(student.getMotherOccupation())
                .motherPhone(student.getMotherPhone())
                .motherEmail(student.getMotherEmail())
                .guardianName(student.getGuardianName())
                .guardianRelation(student.getGuardianRelation())
                .guardianPhone(student.getGuardianPhone())
                .guardianEmail(student.getGuardianEmail())
                .previousSchool(student.getPreviousSchool())
                .previousClass(student.getPreviousClass())
                .previousPercentage(student.getPreviousPercentage())
                .medicalConditions(student.getMedicalConditions())
                .allergies(student.getAllergies())
                .notes(student.getNotes())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    private String generateAdmissionNumber() {
        Integer maxNumber = studentRepository.findMaxAdmissionNumber();
        int nextNumber = (maxNumber != null) ? maxNumber + 1 : 1;
        return String.format("ADM%06d", nextNumber);
    }

    private String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }
}
