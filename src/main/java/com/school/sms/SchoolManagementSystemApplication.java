package com.school.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for School Management System
 * 
 * @EnableJpaAuditing: Enables automatic population of audit fields
 *                     (createdDate, modifiedDate)
 * @EnableCaching: Enables Spring's annotation-driven cache management
 * @EnableAsync: Enables asynchronous method execution
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class SchoolManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementSystemApplication.class, args);
    }
}
