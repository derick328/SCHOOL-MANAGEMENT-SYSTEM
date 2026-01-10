# 📁 Project Structure

```
SCHOOL-MANAGEMENT-SYSTEM/
│
├── src/
│   ├── main/
│   │   ├── java/com/school/sms/
│   │   │   │
│   │   │   ├── SchoolManagementSystemApplication.java  # Main entry point
│   │   │   │
│   │   │   ├── config/                    # Configuration classes
│   │   │   │   ├── SecurityConfig.java    # Spring Security configuration
│   │   │   │   ├── AuditConfig.java       # JPA auditing configuration
│   │   │   │   └── DataInitializerConfig.java  # Initial data setup
│   │   │   │
│   │   │   ├── security/                  # Security components
│   │   │   │   ├── JwtTokenProvider.java  # JWT token utilities
│   │   │   │   ├── JwtAuthenticationFilter.java  # JWT filter
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   ├── UserDetailsImpl.java   # Custom UserDetails
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   │
│   │   │   ├── auth/                      # Authentication module
│   │   │   │   ├── entity/
│   │   │   │   │   └── User.java          # User entity
│   │   │   │   ├── repository/
│   │   │   │   │   └── UserRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   └── AuthService.java   # Auth business logic
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthController.java # Auth REST endpoints
│   │   │   │   └── dto/                   # Data Transfer Objects
│   │   │   │       ├── LoginRequest.java
│   │   │   │       ├── LoginResponse.java
│   │   │   │       ├── RegisterRequest.java
│   │   │   │       ├── ForgotPasswordRequest.java
│   │   │   │       ├── ResetPasswordRequest.java
│   │   │   │       ├── ChangePasswordRequest.java
│   │   │   │       └── RefreshTokenRequest.java
│   │   │   │
│   │   │   ├── audit/                     # Audit logging module
│   │   │   │   ├── entity/
│   │   │   │   │   └── AuditLog.java      # Audit log entity
│   │   │   │   ├── repository/
│   │   │   │   │   └── AuditLogRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   └── AuditService.java  # Audit operations
│   │   │   │   └── aspect/
│   │   │   │       └── AuditAspect.java   # AOP for auto-logging
│   │   │   │
│   │   │   ├── student/                   # Student management module
│   │   │   │   └── entity/
│   │   │   │       └── Student.java       # Student entity
│   │   │   │
│   │   │   ├── teacher/                   # [To be implemented]
│   │   │   ├── classmanagement/           # [To be implemented]
│   │   │   ├── attendance/                # [To be implemented]
│   │   │   ├── exam/                      # [To be implemented]
│   │   │   ├── fees/                      # [To be implemented]
│   │   │   ├── notification/              # [To be implemented]
│   │   │   │
│   │   │   └── common/                    # Shared components
│   │   │       ├── entity/
│   │   │       │   └── BaseEntity.java    # Base audit fields
│   │   │       ├── enums/                 # Enumerations
│   │   │       │   ├── Role.java
│   │   │       │   ├── AccountStatus.java
│   │   │       │   ├── Gender.java
│   │   │       │   ├── StudentStatus.java
│   │   │       │   └── BloodGroup.java
│   │   │       ├── dto/
│   │   │       │   └── ApiResponse.java   # Standard API response
│   │   │       ├── exception/             # Custom exceptions
│   │   │       │   ├── AppException.java
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   ├── BadRequestException.java
│   │   │       │   ├── AuthenticationException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       └── util/
│   │   │           └── SecurityUtils.java # Security helper methods
│   │   │
│   │   └── resources/
│   │       ├── application.yml            # Main configuration
│   │       ├── application.properties     # App metadata
│   │       └── templates/                 # Thymeleaf templates (future)
│   │
│   └── test/                              # Test classes
│       └── java/com/school/sms/
│
├── pom.xml                                # Maven dependencies
├── .gitignore                             # Git ignore rules
├── README.md                              # Project documentation
├── GETTING_STARTED.txt                    # Quick start guide
└── API_TESTING_GUIDE.md                   # API testing examples

```

## 📊 Module Overview

### ✅ Completed Modules

#### 1. Authentication & Security
- **Location**: `com.school.sms.auth`, `com.school.sms.security`
- **Features**:
  - JWT-based authentication
  - User registration & login
  - Password management (change, reset, forgot)
  - Token refresh mechanism
  - BCrypt password hashing
  - Account lockout protection
  
#### 2. Audit Logging
- **Location**: `com.school.sms.audit`
- **Features**:
  - Automatic action logging with AOP
  - IP address & user agent tracking
  - Success/failure tracking
  - Date-based audit queries

#### 3. Student Management Foundation
- **Location**: `com.school.sms.student`
- **Features**:
  - Comprehensive student entity
  - Personal information storage
  - Parent/Guardian details
  - Medical records support
  - Document storage links

#### 4. Common Infrastructure
- **Location**: `com.school.sms.common`
- **Features**:
  - Base entity with audit fields
  - Standard API response format
  - Global exception handling
  - Custom exceptions
  - Security utilities
  - Common enumerations

### 🔜 Modules to Implement

1. **Teacher Management**: Teacher profiles, subject allocation
2. **Class Management**: Classes, sections, subjects, timetables
3. **Attendance System**: Student/teacher attendance tracking
4. **Exam System**: Exam creation, marks entry, GPA calculation
5. **Fee Management**: Fee structures, payment tracking
6. **Notification System**: Email/SMS notifications
7. **Dashboard**: Role-based dashboards with analytics

## 🗄️ Database Schema

### Core Tables

#### users
- Primary authentication table
- Fields: username, email, password (hashed), role, account status
- Security fields: failed login attempts, lockout timestamp
- Token fields: password reset, email verification

#### audit_logs
- Tracks all user actions
- Fields: username, action, entity type, entity ID, IP address
- Indexed on username, action, entity type, created date

#### students
- Extended student profiles
- Links to users table (OneToOne)
- Comprehensive personal, academic, and medical information

## 🔧 Key Technologies

### Backend
- **Spring Boot 3.2.1**: Framework
- **Spring Security**: Authentication & authorization
- **Spring Data JPA**: Database access
- **PostgreSQL**: Database
- **JWT (JJWT 0.12.3)**: Token generation
- **Lombok**: Boilerplate reduction
- **AspectJ**: AOP for audit logging

### Build & Development
- **Maven**: Build tool
- **Spring Boot DevTools**: Hot reload
- **Spring Boot Actuator**: Monitoring

## 📝 Code Organization Principles

### Layer Architecture
```
Controller → Service → Repository → Database
     ↓          ↓
    DTO       Entity
```

### Responsibilities
- **Controllers**: Handle HTTP requests/responses
- **Services**: Business logic
- **Repositories**: Database operations
- **Entities**: Database models
- **DTOs**: Data transfer between layers
- **Exceptions**: Error handling

### Best Practices Applied
✅ Never expose entities directly (use DTOs)
✅ Validate all input (Bean Validation)
✅ Use transactions appropriately
✅ Log important operations
✅ Handle exceptions globally
✅ Use enums for fixed values
✅ Index database columns
✅ Use optimistic locking
✅ Implement audit trails

## 🎯 Next Development Steps

1. **Complete Student CRUD**: Add repository, service, controller, DTOs
2. **Implement Teacher Module**: Similar structure to Student
3. **Build Class Management**: Classes, sections, subjects
4. **Create Attendance System**: Daily attendance tracking
5. **Develop Exam System**: With grade calculation
6. **Build Fee Management**: Payment tracking
7. **Implement Notifications**: Email service
8. **Create Dashboards**: Role-based views with charts
9. **Add File Upload**: Profile photos, documents
10. **Generate Reports**: PDF report cards, attendance reports

---

**Project Status**: 🚧 Foundation Complete - Ready for Module Development

**Code Quality**: ✅ Production-ready security & architecture

**Documentation**: ✅ Comprehensive README & guides
