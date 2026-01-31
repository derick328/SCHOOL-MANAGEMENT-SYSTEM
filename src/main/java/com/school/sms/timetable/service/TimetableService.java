package com.school.sms.timetable.service;

import com.school.sms.common.exception.ResourceNotFoundException;
import com.school.sms.teacher.entity.Teacher;
import com.school.sms.teacher.repository.TeacherRepository;
import com.school.sms.timetable.dto.*;
import com.school.sms.timetable.entity.Timetable;
import com.school.sms.timetable.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Timetable management with conflict detection
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final TeacherRepository teacherRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public TimetableResponse createTimetable(TimetableRequest request) {
        log.info("Creating timetable for class: {}, subject: {}", request.getClassName(), request.getSubject());

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));

        // Check for conflicts
        ConflictCheckResponse conflictCheck = checkConflicts(request, null);
        if (conflictCheck.getHasConflict()) {
            throw new IllegalStateException("Cannot create timetable: " + conflictCheck.getMessage());
        }

        // Create timetable
        Timetable timetable = Timetable.builder()
                .className(request.getClassName())
                .section(request.getSection())
                .subject(request.getSubject())
                .teacher(teacher)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .room(request.getRoom())
                .notes(request.getNotes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        timetable = timetableRepository.save(timetable);
        log.info("Timetable created successfully with id: {}", timetable.getId());

        return mapToResponse(timetable);
    }

    @Transactional
    public TimetableResponse updateTimetable(Long id, TimetableRequest request) {
        log.info("Updating timetable with id: {}", id);

        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable not found with id: " + id));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));

        // Check for conflicts (excluding current timetable)
        ConflictCheckResponse conflictCheck = checkConflicts(request, id);
        if (conflictCheck.getHasConflict()) {
            throw new IllegalStateException("Cannot update timetable: " + conflictCheck.getMessage());
        }

        // Update timetable
        timetable.setClassName(request.getClassName());
        timetable.setSection(request.getSection());
        timetable.setSubject(request.getSubject());
        timetable.setTeacher(teacher);
        timetable.setDayOfWeek(request.getDayOfWeek());
        timetable.setStartTime(request.getStartTime());
        timetable.setEndTime(request.getEndTime());
        timetable.setRoom(request.getRoom());
        timetable.setNotes(request.getNotes());
        if (request.getIsActive() != null) {
            timetable.setIsActive(request.getIsActive());
        }

        timetable = timetableRepository.save(timetable);
        log.info("Timetable updated successfully");

        return mapToResponse(timetable);
    }

    @Transactional(readOnly = true)
    public TimetableResponse getTimetable(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable not found with id: " + id));
        return mapToResponse(timetable);
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getAllTimetables() {
        return timetableRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTimetableByClass(String className, String section) {
        List<Timetable> timetables;
        if (section != null && !section.isEmpty()) {
            timetables = timetableRepository.findByClassNameAndSectionAndIsActiveTrue(className, section);
        } else {
            timetables = timetableRepository.findByClassNameAndIsActiveTrue(className);
        }
        return timetables.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTimetableByTeacher(Long teacherId) {
        return timetableRepository.findByTeacher_IdAndIsActiveTrue(teacherId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getTimetableByDay(DayOfWeek dayOfWeek) {
        return timetableRepository.findByDayOfWeekAndIsActiveTrue(dayOfWeek).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTimetable(Long id) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable not found with id: " + id));
        timetable.setIsActive(false);
        timetableRepository.save(timetable);
        log.info("Timetable deactivated successfully with id: {}", id);
    }

    /**
     * Check for scheduling conflicts
     */
    @Transactional(readOnly = true)
    public ConflictCheckResponse checkConflicts(TimetableRequest request, Long excludeTimetableId) {
        List<ConflictCheckResponse.ConflictDetail> conflicts = new ArrayList<>();

        // Validate time range
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            conflicts.add(ConflictCheckResponse.ConflictDetail.builder()
                    .type("TIME_VALIDATION")
                    .description("Start time must be before end time")
                    .build());
        }

        // Check teacher conflicts
        List<Timetable> teacherConflicts = timetableRepository.findTeacherConflicts(
                request.getTeacherId(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime()
        );

        // Exclude current timetable if updating
        if (excludeTimetableId != null) {
            teacherConflicts = teacherConflicts.stream()
                    .filter(t -> !t.getId().equals(excludeTimetableId))
                    .collect(Collectors.toList());
        }

        for (Timetable conflict : teacherConflicts) {
            conflicts.add(ConflictCheckResponse.ConflictDetail.builder()
                    .type("TEACHER_CONFLICT")
                    .description(String.format("Teacher already has a class at this time: %s (%s - %s)",
                            conflict.getSubject(), conflict.getClassName(), conflict.getSection()))
                    .conflictingSlot(mapToResponse(conflict))
                    .build());
        }

        // Check class conflicts
        List<Timetable> classConflicts = timetableRepository.findClassConflicts(
                request.getClassName(),
                request.getSection(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (excludeTimetableId != null) {
            classConflicts = classConflicts.stream()
                    .filter(t -> !t.getId().equals(excludeTimetableId))
                    .collect(Collectors.toList());
        }

        for (Timetable conflict : classConflicts) {
            conflicts.add(ConflictCheckResponse.ConflictDetail.builder()
                    .type("CLASS_CONFLICT")
                    .description(String.format("Class already has a lesson at this time: %s with %s",
                            conflict.getSubject(), conflict.getTeacher().getUser().getFullName()))
                    .conflictingSlot(mapToResponse(conflict))
                    .build());
        }

        // Check room conflicts (if room is specified)
        if (request.getRoom() != null && !request.getRoom().isEmpty()) {
            List<Timetable> roomConflicts = timetableRepository.findRoomConflicts(
                    request.getRoom(),
                    request.getDayOfWeek(),
                    request.getStartTime(),
                    request.getEndTime()
            );

            if (excludeTimetableId != null) {
                roomConflicts = roomConflicts.stream()
                        .filter(t -> !t.getId().equals(excludeTimetableId))
                        .collect(Collectors.toList());
            }

            for (Timetable conflict : roomConflicts) {
                conflicts.add(ConflictCheckResponse.ConflictDetail.builder()
                        .type("ROOM_CONFLICT")
                        .description(String.format("Room is already occupied: %s - %s",
                                conflict.getClassName(), conflict.getSubject()))
                        .conflictingSlot(mapToResponse(conflict))
                        .build());
            }
        }

        boolean hasConflict = !conflicts.isEmpty();
        String message = hasConflict
                ? String.format("Found %d conflict(s)", conflicts.size())
                : "No conflicts found";

        return ConflictCheckResponse.builder()
                .hasConflict(hasConflict)
                .conflicts(conflicts)
                .message(message)
                .build();
    }

    private TimetableResponse mapToResponse(Timetable timetable) {
        return TimetableResponse.builder()
                .id(timetable.getId())
                .className(timetable.getClassName())
                .section(timetable.getSection())
                .subject(timetable.getSubject())
                .teacherId(timetable.getTeacher().getId())
                .teacherName(timetable.getTeacher().getUser().getFullName())
                .dayOfWeek(timetable.getDayOfWeek())
                .startTime(timetable.getStartTime())
                .endTime(timetable.getEndTime())
                .room(timetable.getRoom())
                .notes(timetable.getNotes())
                .isActive(timetable.getIsActive())
                .createdAt(timetable.getCreatedAt() != null ? timetable.getCreatedAt().format(FORMATTER) : null)
                .updatedAt(timetable.getUpdatedAt() != null ? timetable.getUpdatedAt().format(FORMATTER) : null)
                .build();
    }
}
