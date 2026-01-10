package com.school.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for School Management System
 * 
 * @EnableCaching: Enables Spring's annotation-driven cache management
 * @EnableAsync: Enables asynchronous method execution
 *               Note: JPA Auditing is configured in AuditConfig.java
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class SchoolManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementSystemApplication.class, args);
    }
}
