package com.school.sms.config;

import com.school.sms.auth.entity.User;
import com.school.sms.auth.repository.UserRepository;
import com.school.sms.common.enums.AccountStatus;
import com.school.sms.common.enums.Gender;
import com.school.sms.common.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Data initialization configuration
 * Creates default admin user on first startup
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Create default admin user if not exists
            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@school.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .firstName("System")
                        .lastName("Administrator")
                        .phoneNumber("0000000000")
                        .role(Role.ADMIN)
                        .gender(Gender.MALE)
                        .accountStatus(AccountStatus.ACTIVE)
                        .emailVerified(true)
                        .failedLoginAttempts(0)
                        .passwordExpired(false)
                        .passwordChangedAt(LocalDateTime.now())
                        .active(true)
                        .build();

                userRepository.save(admin);
                log.info("✅ Default admin user created successfully!");
                log.info("   Username: admin");
                log.info("   Password: Admin@123");
                log.info("   ⚠️  Please change this password immediately!");
            }

            // Create demo teacher user
            if (!userRepository.existsByUsername("teacher_demo")) {
                User teacher = User.builder()
                        .username("teacher_demo")
                        .email("teacher@school.com")
                        .password(passwordEncoder.encode("Teacher@123"))
                        .firstName("John")
                        .lastName("Smith")
                        .phoneNumber("1234567890")
                        .role(Role.TEACHER)
                        .gender(Gender.MALE)
                        .accountStatus(AccountStatus.ACTIVE)
                        .emailVerified(true)
                        .failedLoginAttempts(0)
                        .passwordExpired(false)
                        .passwordChangedAt(LocalDateTime.now())
                        .active(true)
                        .build();

                userRepository.save(teacher);
                log.info("✅ Demo teacher user created successfully!");
                log.info("   Username: teacher_demo");
                log.info("   Password: Teacher@123");
            }

            // Create demo student user
            if (!userRepository.existsByUsername("student_demo")) {
                User student = User.builder()
                        .username("student_demo")
                        .email("student@school.com")
                        .password(passwordEncoder.encode("Student@123"))
                        .firstName("Jane")
                        .lastName("Doe")
                        .phoneNumber("0987654321")
                        .role(Role.STUDENT)
                        .gender(Gender.FEMALE)
                        .accountStatus(AccountStatus.ACTIVE)
                        .emailVerified(true)
                        .failedLoginAttempts(0)
                        .passwordExpired(false)
                        .passwordChangedAt(LocalDateTime.now())
                        .active(true)
                        .build();

                userRepository.save(student);
                log.info("✅ Demo student user created successfully!");
                log.info("   Username: student_demo");
                log.info("   Password: Student@123");
            }

            log.info("========================================");
            log.info("🎓 School Management System Ready!");
            log.info("========================================");
        };
    }
}
