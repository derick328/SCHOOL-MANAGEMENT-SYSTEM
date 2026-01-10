package com.school.sms.audit.repository;

import com.school.sms.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Audit Log Repository
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUsername(String username, Pageable pageable);

    Page<AuditLog> findByAction(String action, Pageable pageable);

    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate")
    Page<AuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<AuditLog> findTop10ByUsernameOrderByCreatedAtDesc(String username);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.username = :username AND a.action = :action")
    Long countByUsernameAndAction(String username, String action);
}
