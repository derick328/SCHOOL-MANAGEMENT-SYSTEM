package com.school.sms.teacher.repository;

import com.school.sms.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Teacher entity
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmployeeId(String employeeId);

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByUserId(Long userId);

    List<Teacher> findByEmploymentStatus(Teacher.EmploymentStatus status);

    @Query("SELECT t FROM Teacher t WHERE " +
            "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.employeeId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Teacher> searchTeachers(@Param("search") String search);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String email);
}
