package com.school.sms.common.enums;

/**
 * Account status enumeration
 * Used to track user account state
 */
public enum AccountStatus {
    ACTIVE("Active", "Account is active and can login"),
    INACTIVE("Inactive", "Account is temporarily disabled"),
    LOCKED("Locked", "Account is locked due to security reasons"),
    PENDING("Pending", "Account is pending approval"),
    SUSPENDED("Suspended", "Account has been suspended");

    private final String displayName;
    private final String description;

    AccountStatus(String displayName, String description) {
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
