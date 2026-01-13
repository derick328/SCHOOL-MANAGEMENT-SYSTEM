package com.school.sms.common.enums;

/**
 * System-wide user roles with hierarchical authority
 * Each role has specific permissions defined in SecurityConfig
 */
public enum Role {
    ADMIN("Administrator", "Full system access"),
    TEACHER("Teacher", "Access to teaching resources and student data"),
    ACADEMIC_TEACHER("Academic Teacher", "Teacher with additional academic management responsibilities"),
    DISCIPLINE_TEACHER("Discipline Teacher", "Teacher with additional student discipline management responsibilities"),
    STUDENT("Student", "Access to personal academic information"),
    PARENT("Parent", "Access to child's academic information");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get role authority for Spring Security
     * Spring Security expects roles to be prefixed with ROLE_
     */
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
