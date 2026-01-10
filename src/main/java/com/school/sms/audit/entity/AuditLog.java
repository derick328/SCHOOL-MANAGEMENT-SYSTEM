package com.school.sms.audit.entity;

import com.school.sms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Audit Log Entity
 * Tracks all user actions in the system for security and compliance
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_user", columnList = "username"),
        @Index(name = "idx_audit_action", columnList = "action"),
        @Index(name = "idx_audit_entity", columnList = "entityType, entityId"),
        @Index(name = "idx_audit_created", columnList = "createdAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String username; // User who performed the action

    @Column(nullable = false, length = 50)
    private String action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, etc.

    @Column(length = 100)
    private String entityType; // Student, Teacher, Exam, etc.

    @Column
    private Long entityId; // ID of the affected entity

    @Column(columnDefinition = "TEXT")
    private String details; // Additional details in JSON format

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Column
    private Boolean success = true;

    @Column(columnDefinition = "TEXT")
    private String errorMessage; // If action failed
}
