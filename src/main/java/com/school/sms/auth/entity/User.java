package com.school.sms.auth.entity;

import com.school.sms.common.entity.BaseEntity;
import com.school.sms.common.enums.AccountStatus;
import com.school.sms.common.enums.Gender;
import com.school.sms.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User entity - core authentication entity
 * Stores user credentials and basic profile information
 * Related entities (Student, Teacher, Parent) reference this entity
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_role", columnList = "role")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password; // BCrypt hashed

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    @Size(max = 15)
    @Column(length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column
    private String profilePhotoUrl;

    // Security fields
    @Column(nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column
    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column
    private LocalDateTime accountLockedUntil;

    @Column
    private LocalDateTime passwordChangedAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean passwordExpired = false;

    // Password reset fields
    @Column
    private String passwordResetToken;

    @Column
    private LocalDateTime passwordResetTokenExpiry;

    // Email verification fields
    @Column
    private String emailVerificationToken;

    @Column
    private LocalDateTime emailVerificationTokenExpiry;

    /**
     * Get full name
     */
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Check if account is locked
     */
    @Transient
    public boolean isAccountLocked() {
        return accountLockedUntil != null && accountLockedUntil.isAfter(LocalDateTime.now());
    }

    /**
     * Check if account is active and not locked
     */
    @Transient
    public boolean canLogin() {
        return accountStatus == AccountStatus.ACTIVE
                && !isAccountLocked()
                && !passwordExpired;
    }
}
