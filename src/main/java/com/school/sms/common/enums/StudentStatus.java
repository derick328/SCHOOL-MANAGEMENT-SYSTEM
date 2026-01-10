package com.school.sms.common.enums;

/**
 * Student status enumeration
 */
public enum StudentStatus {
    ACTIVE("Active", "Currently enrolled"),
    INACTIVE("Inactive", "Not currently enrolled"),
    GRADUATED("Graduated", "Completed studies"),
    SUSPENDED("Suspended", "Temporarily suspended"),
    EXPELLED("Expelled", "Permanently expelled"),
    TRANSFERRED("Transferred", "Transferred to another school");

    private final String displayName;
    private final String description;

    StudentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
