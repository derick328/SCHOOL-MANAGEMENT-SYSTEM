package com.school.sms.student.repository;

import com.school.sms.common.enums.StudentStatus;
import com.school.sms.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Student entity
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByAdmissionNumber(String admissionNumber);

    Optional<Student> findByRollNumber(String rollNumber);

    Optional<Student> findByUserId(Long userId);

    List<Student> findByStatus(StudentStatus status);

    List<Student> findByCurrentClass(String currentClass);

    List<Student> findByCurrentClassAndSection(String currentClass, String section);

    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.user.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.user.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.admissionNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.currentClass) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Student> searchStudents(@Param("search") String search);

    boolean existsByAdmissionNumber(String admissionNumber);

    boolean existsByRollNumber(String rollNumber);

    @Query("SELECT MAX(CAST(SUBSTRING(s.admissionNumber, 4) AS int)) FROM Student s WHERE s.admissionNumber LIKE 'ADM%'")
    Integer findMaxAdmissionNumber();
}
