package com.school.sms.timetable.repository;

import com.school.sms.timetable.entity.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for StudentResult entity
 */
@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    List<StudentResult> findByStudent_Id(Long studentId);

    List<StudentResult> findByStudent_IdAndIsPublishedTrue(Long studentId);

    List<StudentResult> findByStudent_IdAndAcademicYear(Long studentId, String academicYear);

    List<StudentResult> findByStudent_IdAndAcademicYearAndTerm(
            Long studentId, String academicYear, String term);

    List<StudentResult> findByExamTypeAndAcademicYear(String examType, String academicYear);

    List<StudentResult> findByStudent_CurrentClassAndStudent_Section(String className, String section);
}
