package com.school.sms.config;

import com.school.sms.auth.entity.User;
import com.school.sms.auth.repository.UserRepository;
import com.school.sms.common.enums.AccountStatus;
import com.school.sms.common.enums.BloodGroup;
import com.school.sms.common.enums.Gender;
import com.school.sms.common.enums.Role;
import com.school.sms.common.enums.StudentStatus;
import com.school.sms.student.entity.Student;
import com.school.sms.student.repository.StudentRepository;
import com.school.sms.teacher.entity.Teacher;
import com.school.sms.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Data Initializer - Creates test students and teachers
 * 30 students per class (classes 1-7) = 210 total students
 * 14 teachers (2 per class)
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String[] FIRST_NAMES_MALE = {
            "James", "John", "Michael", "David", "Daniel", "Matthew", "Andrew", "Joseph",
            "William", "Christopher", "Joshua", "Anthony", "Robert", "Kevin", "Brian",
            "Thomas", "Charles", "Steven", "Edward", "Mark", "George", "Paul", "Peter",
            "Samuel", "Benjamin", "Nicholas", "Alexander", "Ryan", "Tyler", "Jacob",
            "Ethan", "Noah", "Lucas", "Mason", "Oliver", "Liam", "Henry", "Sebastian",
            "Jack", "Owen", "Nathan", "Caleb", "Isaac", "Luke", "Dylan", "Aaron",
            "Gabriel", "Julian", "Adrian", "Eli"
    };

    private static final String[] FIRST_NAMES_FEMALE = {
            "Emma", "Olivia", "Sophia", "Isabella", "Mia", "Charlotte", "Amelia", "Harper",
            "Evelyn", "Abigail", "Emily", "Elizabeth", "Sofia", "Avery", "Ella", "Scarlett",
            "Grace", "Victoria", "Chloe", "Camila", "Penelope", "Riley", "Layla", "Lily",
            "Aria", "Zoey", "Nora", "Hannah", "Lillian", "Eleanor", "Hazel", "Aurora",
            "Savannah", "Audrey", "Brooklyn", "Bella", "Claire", "Lucy", "Skylar", "Paisley",
            "Anna", "Caroline", "Genesis", "Aaliyah", "Kennedy", "Allison", "Maya", "Sarah",
            "Madelyn", "Adeline"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
            "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
            "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
            "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell",
            "Carter", "Roberts"
    };

    private static final String[] SECTIONS = { "A", "B" };

    private static final BloodGroup[] BLOOD_GROUPS = BloodGroup.values();

    private static final String[] SUBJECTS = {
            "Mathematics", "English", "Science", "Social Studies", "Art",
            "Music", "Physical Education", "Computer Science", "History",
            "Geography", "Biology", "Chemistry", "Physics", "Literature"
    };

    private static final String[] QUALIFICATIONS = {
            "B.Ed", "M.Ed", "B.Sc", "M.Sc", "B.A", "M.A", "Ph.D"
    };

    private final Random random = new Random(42); // Fixed seed for reproducibility

    @Bean
    public CommandLineRunner initTestData() {
        return args -> {
            // Check if test students already exist
            long currentCount = studentRepository.count();
            if (currentCount >= 200) {
                log.info("Test students already exist ({} students found). Skipping initialization.", currentCount);
                return;
            }

            log.info("Starting test data initialization...");
            log.info("Current student count: {}. Creating 210 test students (30 per class for classes 1-7)...",
                    currentCount);

            int totalCreated = 0;
            List<User> usersToSave = new ArrayList<>();
            List<Student> studentsToSave = new ArrayList<>();

            for (int classNum = 1; classNum <= 7; classNum++) {
                for (int studentNum = 1; studentNum <= 30; studentNum++) {
                    try {
                        boolean isMale = random.nextBoolean();
                        Gender gender = isMale ? Gender.MALE : Gender.FEMALE;

                        String firstName = isMale
                                ? FIRST_NAMES_MALE[random.nextInt(FIRST_NAMES_MALE.length)]
                                : FIRST_NAMES_FEMALE[random.nextInt(FIRST_NAMES_FEMALE.length)];
                        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

                        String section = SECTIONS[studentNum <= 15 ? 0 : 1]; // First 15 in A, rest in B
                        String username = String.format("student_c%d_%02d", classNum, studentNum);
                        String email = String.format("student.c%d.%02d@school.edu", classNum, studentNum);
                        String admissionNumber = String.format("ADM2026%d%03d", classNum, studentNum);
                        String rollNumber = String.format("C%d%s%02d", classNum, section,
                                studentNum <= 15 ? studentNum : studentNum - 15);

                        // Skip if user already exists
                        if (userRepository.existsByUsername(username)) {
                            continue;
                        }

                        // Create User
                        User user = User.builder()
                                .username(username)
                                .email(email)
                                .password(passwordEncoder.encode("Student@123"))
                                .firstName(firstName)
                                .lastName(lastName)
                                .role(Role.STUDENT)
                                .gender(gender)
                                .accountStatus(AccountStatus.ACTIVE)
                                .emailVerified(true)
                                .passwordExpired(false)
                                .build();

                        user = userRepository.save(user);

                        // Calculate age appropriate date of birth (Class 1 = 6 years, Class 7 = 12
                        // years)
                        int age = 5 + classNum;
                        LocalDate dob = LocalDate.now().minusYears(age).minusMonths(random.nextInt(12))
                                .minusDays(random.nextInt(28));

                        // Create Student
                        Student student = Student.builder()
                                .user(user)
                                .admissionNumber(admissionNumber)
                                .rollNumber(rollNumber)
                                .dateOfBirth(dob)
                                .gender(gender)
                                .bloodGroup(BLOOD_GROUPS[random.nextInt(BLOOD_GROUPS.length)])
                                .currentClass("Class " + classNum)
                                .section(section)
                                .admissionDate(LocalDate.of(2026, 1, 15))
                                .status(StudentStatus.ACTIVE)
                                .nationality("American")
                                .address(String.format("%d Main Street, Apt %d", 100 + random.nextInt(900),
                                        random.nextInt(50) + 1))
                                .city("Springfield")
                                .state("Illinois")
                                .postalCode(String.format("%05d", 60000 + random.nextInt(999)))
                                .country("USA")
                                .fatherName(FIRST_NAMES_MALE[random.nextInt(FIRST_NAMES_MALE.length)] + " " + lastName)
                                .fatherPhone(String.format("55512%04d", random.nextInt(10000)))
                                .fatherEmail(String.format("parent.%s@email.com", username))
                                .motherName(
                                        FIRST_NAMES_FEMALE[random.nextInt(FIRST_NAMES_FEMALE.length)] + " " + lastName)
                                .motherPhone(String.format("55513%04d", random.nextInt(10000)))
                                .build();

                        studentRepository.save(student);
                        totalCreated++;

                        if (totalCreated % 30 == 0) {
                            log.info("Created {} students so far (Class {} complete)", totalCreated, classNum);
                        }

                    } catch (Exception e) {
                        log.error("Error creating student for Class {} #{}: {}", classNum, studentNum, e.getMessage());
                    }
                }
            }

            log.info("===================================================");
            log.info("Test data initialization complete!");
            log.info("Total students created: {}", totalCreated);
            log.info("Classes: 1-7, Students per class: 30");
            log.info("Sections: A (students 1-15), B (students 16-30)");
            log.info("===================================================");
            log.info("Login credentials for any student:");
            log.info("  Username pattern: student_c[CLASS]_[NUMBER]");
            log.info("  Example: student_c1_01 (Class 1, Student 1)");
            log.info("  Password: Student@123");
            log.info("===================================================");

            // Create test teachers
            createTestTeachers();
        };
    }

    private void createTestTeachers() {
        long teacherCount = teacherRepository.count();
        if (teacherCount >= 14) {
            log.info("Test teachers already exist ({} teachers found). Skipping teacher initialization.", teacherCount);
            return;
        }

        log.info("Creating 14 test teachers (2 per class for classes 1-7)...");

        int teachersCreated = 0;

        for (int i = 1; i <= 14; i++) {
            try {
                boolean isMale = random.nextBoolean();
                Gender gender = isMale ? Gender.MALE : Gender.FEMALE;

                String firstName = isMale
                        ? FIRST_NAMES_MALE[random.nextInt(FIRST_NAMES_MALE.length)]
                        : FIRST_NAMES_FEMALE[random.nextInt(FIRST_NAMES_FEMALE.length)];
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

                String username = String.format("teacher_%02d", i);
                String email = String.format("teacher.%02d@school.edu", i);
                String employeeId = String.format("EMP%04d", 1000 + i);

                // Skip if user already exists
                if (userRepository.existsByUsername(username)) {
                    log.info("Teacher {} already exists, skipping...", username);
                    continue;
                }

                // Create User with TEACHER role (not ACADEMIC_TEACHER or PRINCIPAL)
                User user = User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode("Teacher@123"))
                        .firstName(firstName)
                        .lastName(lastName)
                        .role(Role.TEACHER) // Regular teacher role only
                        .gender(gender)
                        .accountStatus(AccountStatus.ACTIVE)
                        .emailVerified(true)
                        .passwordExpired(false)
                        .build();

                user = userRepository.save(user);

                // Calculate date of birth (teachers are 25-50 years old)
                int age = 25 + random.nextInt(26);
                LocalDate dob = LocalDate.now().minusYears(age).minusMonths(random.nextInt(12))
                        .minusDays(random.nextInt(28));

                // Assign 2-3 subjects to each teacher
                int numSubjects = 2 + random.nextInt(2);
                StringBuilder subjectsBuilder = new StringBuilder();
                for (int j = 0; j < numSubjects; j++) {
                    if (j > 0)
                        subjectsBuilder.append(", ");
                    subjectsBuilder.append(SUBJECTS[(i + j) % SUBJECTS.length]);
                }

                // Create Teacher
                Teacher teacher = Teacher.builder()
                        .user(user)
                        .employeeId(employeeId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .phone(String.format("55514%04d", random.nextInt(10000)))
                        .dateOfBirth(dob)
                        .gender(isMale ? Teacher.Gender.MALE : Teacher.Gender.FEMALE)
                        .address(String.format("%d Oak Avenue, Suite %d", 200 + random.nextInt(800),
                                random.nextInt(20) + 1))
                        .qualification(QUALIFICATIONS[random.nextInt(QUALIFICATIONS.length)])
                        .specialization(subjectsBuilder.toString())
                        .experienceYears(random.nextInt(20) + 1)
                        .joiningDate(
                                LocalDate.of(2020 + random.nextInt(6), 1 + random.nextInt(12), 1 + random.nextInt(28)))
                        .salary(35000.0 + random.nextInt(25000))
                        .employmentStatus(Teacher.EmploymentStatus.ACTIVE)
                        .subjects(subjectsBuilder.toString())
                        .build();

                teacherRepository.save(teacher);
                teachersCreated++;

                log.info("Created teacher {}: {} {} ({})", i, firstName, lastName, subjectsBuilder);

            } catch (Exception e) {
                log.error("Error creating teacher #{}: {}", i, e.getMessage());
            }
        }

        log.info("===================================================");
        log.info("Teacher initialization complete!");
        log.info("Total teachers created: {}", teachersCreated);
        log.info("===================================================");
        log.info("Login credentials for any teacher:");
        log.info("  Username pattern: teacher_[NUMBER]");
        log.info("  Example: teacher_01, teacher_02, ... teacher_14");
        log.info("  Password: Teacher@123");
        log.info("===================================================");
    }
}
