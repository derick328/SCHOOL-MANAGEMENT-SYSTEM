package com.school.sms.timetable.repository;

import com.school.sms.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository for Timetable entity
 */
@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByClassNameAndSectionAndIsActiveTrue(String className, String section);

    List<Timetable> findByClassNameAndIsActiveTrue(String className);

    List<Timetable> findByTeacher_IdAndIsActiveTrue(Long teacherId);

    List<Timetable> findByDayOfWeekAndIsActiveTrue(DayOfWeek dayOfWeek);

    @Query("SELECT t FROM Timetable t WHERE t.teacher.id = :teacherId " +
           "AND t.dayOfWeek = :dayOfWeek " +
           "AND t.isActive = true " +
           "AND ((t.startTime < :endTime AND t.endTime > :startTime))")
    List<Timetable> findTeacherConflicts(
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT t FROM Timetable t WHERE t.className = :className " +
           "AND (:section IS NULL OR t.section = :section) " +
           "AND t.dayOfWeek = :dayOfWeek " +
           "AND t.isActive = true " +
           "AND ((t.startTime < :endTime AND t.endTime > :startTime))")
    List<Timetable> findClassConflicts(
            @Param("className") String className,
            @Param("section") String section,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT t FROM Timetable t WHERE t.room = :room " +
           "AND t.dayOfWeek = :dayOfWeek " +
           "AND t.isActive = true " +
           "AND ((t.startTime < :endTime AND t.endTime > :startTime))")
    List<Timetable> findRoomConflicts(
            @Param("room") String room,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
