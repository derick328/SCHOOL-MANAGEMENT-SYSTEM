package com.school.sms.audit.service;

import com.school.sms.audit.entity.AuditLog;
import com.school.sms.audit.repository.AuditLogRepository;
import com.school.sms.common.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * Audit Service
 * Handles audit logging operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Log an action
     * Async to not block main transaction
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action, String entityType, Long entityId, String details) {
        try {
            String username = SecurityUtils.getCurrentUsername();
            if (username == null) {
                username = "system";
            }

            AuditLog auditLog = AuditLog.builder()
                    .username(username)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .details(details)
                    .ipAddress(getClientIpAddress())
                    .userAgent(getUserAgent())
                    .success(true)
                    .build();

            auditLogRepository.save(auditLog);

            log.debug("Audit log created: {} - {} - {}", username, action, entityType);

        } catch (Exception ex) {
            log.error("Failed to create audit log", ex);
        }
    }

    /**
     * Log a failed action
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedAction(String action, String entityType, Long entityId, String errorMessage) {
        try {
            String username = SecurityUtils.getCurrentUsername();
            if (username == null) {
                username = "system";
            }

            AuditLog auditLog = AuditLog.builder()
                    .username(username)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .ipAddress(getClientIpAddress())
                    .userAgent(getUserAgent())
                    .success(false)
                    .errorMessage(errorMessage)
                    .build();

            auditLogRepository.save(auditLog);

            log.debug("Failed audit log created: {} - {} - {}", username, action, entityType);

        } catch (Exception ex) {
            log.error("Failed to create failed audit log", ex);
        }
    }

    /**
     * Get audit logs by username
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByUsername(String username, Pageable pageable) {
        return auditLogRepository.findByUsername(username, pageable);
    }

    /**
     * Get audit logs by action
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByAction(String action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable);
    }

    /**
     * Get audit logs by entity
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByEntity(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
    }

    /**
     * Get audit logs by date range
     */
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        return auditLogRepository.findByDateRange(startDate, endDate, pageable);
    }

    /**
     * Get client IP address
     */
    private String getClientIpAddress() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            if (request == null) {
                return "unknown";
            }

            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }

            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty()) {
                return xRealIp;
            }

            return request.getRemoteAddr();
        } catch (Exception ex) {
            return "unknown";
        }
    }

    /**
     * Get user agent
     */
    private String getUserAgent() {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            if (request == null) {
                return "unknown";
            }

            String userAgent = request.getHeader("User-Agent");
            return userAgent != null ? userAgent : "unknown";
        } catch (Exception ex) {
            return "unknown";
        }
    }

    /**
     * Get current HTTP request
     */
    private HttpServletRequest getCurrentHttpRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception ex) {
            return null;
        }
    }
}
