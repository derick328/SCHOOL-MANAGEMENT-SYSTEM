# 🎓 School Management System (SMS)

A production-ready, enterprise-grade School Management System built with **Java 17**, **Spring Boot 3**, and modern security practices.

## 🚀 Features Implemented

### ✅ Core Infrastructure
- **Spring Boot 3.2.1** with Java 17
- **PostgreSQL** database with optimized indexing
- **JWT-based authentication** (access & refresh tokens)
- **Role-Based Access Control** (ADMIN, TEACHER, STUDENT, PARENT)
- **BCrypt password hashing** (strength 12)
- **Audit logging** system with AOP
- **Global exception handling**
- **Request/Response DTOs** with validation
- **Optimistic locking** for concurrency control

### 🔐 Security Features
- JWT token generation & validation
- Password strength requirements (min 8 chars, uppercase, lowercase, digit, special char)
- Account lockout after failed login attempts (configurable)
- Password reset with secure tokens (24-hour expiry)
- Email verification tokens
- Method-level security with `@PreAuthorize`
- CORS configuration
- XSS & SQL injection protection
- Secure headers configuration

### 📦 Modules Implemented

#### 1. Authentication Module (`/api/auth`)
- ✅ User registration with validation
- ✅ Login with JWT tokens
- ✅ Logout
- ✅ Forgot password
- ✅ Reset password
- ✅ Change password
- ✅ Refresh token
- ✅ Get current user profile

#### 2. Audit Logging
- ✅ Automatic logging of user actions
- ✅ IP address & user agent tracking
- ✅ Failed action logging
- ✅ AOP-based audit aspect

#### 3. Student Management (Started)
- ✅ Student entity with comprehensive fields
- ✅ Admission number & roll number
- ✅ Personal information (DOB, gender, blood group)
- ✅ Parent/Guardian details
- ✅ Previous school records
- ✅ Medical information
- ✅ Document storage support

## 🛠️ Technology Stack

### Backend
- **Java**: 17
- **Spring Boot**: 3.2.1
- **Spring Security**: JWT-based authentication
- **Spring Data JPA**: Hibernate ORM
- **PostgreSQL**: Database
- **Maven**: Build tool
- **Lombok**: Reduce boilerplate code
- **MapStruct**: DTO mapping (prepared)
- **JJWT**: JWT implementation (0.12.3)

### Frontend (Prepared)
- **Thymeleaf**: Server-side template engine
- **Bootstrap 5.3.2**: UI framework
- **jQuery 3.7.1**: JavaScript library
- **Font Awesome 6.5.1**: Icons

## 📋 Prerequisites

- **Java JDK 17** or higher
- **PostgreSQL 12** or higher
- **Maven 3.8** or higher
- IDE: IntelliJ IDEA / Eclipse / VS Code

## 🚀 Quick Start

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE school_management_db;

-- Create user (optional)
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE school_management_db TO postgres;
```

### 2. Configure Application

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/school_management_db
    username: postgres
    password: postgres

jwt:
  secret: your-secret-key-change-this-in-production-min-256-bits-for-hs256-algorithm
```

**⚠️ IMPORTANT**: Change the JWT secret in production!

### 3. Build & Run

```bash
# Navigate to project directory
cd SCHOOL-MANAGEMENT-SYSTEM

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login | No |
| POST | `/api/auth/logout` | Logout | Yes |
| POST | `/api/auth/forgot-password` | Request password reset | No |
| POST | `/api/auth/reset-password` | Reset password with token | No |
| POST | `/api/auth/change-password` | Change password | Yes |
| POST | `/api/auth/refresh` | Refresh access token | No |
| GET | `/api/auth/me` | Get current user | Yes |

### Example: Register User

```json
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_student",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "1234567890",
  "role": "STUDENT",
  "gender": "MALE"
}
```

### Example: Login

```json
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "john_student",
  "password": "SecurePass123!",
  "rememberMe": true
}
```

## 🔒 Security Configuration

### Password Requirements
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character (`@#$%^&+=`)

### Account Security
- Max login attempts: **5** (configurable)
- Lockout duration: **30 minutes** (configurable)
- Password expiry: **90 days** (configurable)
- JWT expiration: **24 hours**
- Refresh token expiration: **7 days**

### Role-Based Access

```
ADMIN       → Full system access
TEACHER     → Student data, grades, attendance
STUDENT     → Own academic information
PARENT      → Child's academic information
```

## 🎯 Next Steps

### Planned Modules
- [ ] Complete Student Management (CRUD operations)
- [ ] Teacher Management
- [ ] Class & Subject Management
- [ ] Attendance System
- [ ] Exams & Results (with GPA calculation)
- [ ] Fee Management
- [ ] Notification System (Email/SMS)
- [ ] Dashboard (role-based views)
- [ ] Timetable Management
- [ ] Report Card Generation (PDF)
-[] Upload school timetable
## 📝 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use Lombok to reduce boilerplate
- Always validate input with Bean Validation
- Use DTOs for request/response
- Never expose entities directly

### Security Best Practices
- Always use `@PreAuthorize` for method-level security
- Hash passwords with BCrypt
- Validate all user input
- Use prepared statements (JPA does this)
- Log security events

---

**Status**: 🚧 In Development | **Version**: 1.0.0 | **Last Updated**: January 10, 2026


### admin details
        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)    
2026-01-10 16:28:59 - Γ£à Demo student user created successfully!
2026-01-10 16:28:59 -    Username: student_demo
2026-01-10 16:28:59 -    Password: Student@123
2026-01-10 16:28:59 - ========================================
2026-01-10 16:28:59 - ≡ƒÄô School Management System Ready!
2026-01-10 16:28:59 - ========================================

Default Users in Your System:
1. ADMIN User
Username: admin
Password: Admin@123
Email: admin@school.com
Role: ADMIN
Name: Admin User
Phone: +1234567890
2. TEACHER User
Username: teacher_demo
Password: Teacher@123
Email: teacher@school.com
Role: TEACHER
Name: Demo Teacher
Phone: +1234567891
3. STUDENT User
Username: student_demo
Password: Student@123
Email: student@school.com
Role: STUDENT
Name: Demo Student
Phone: +1234567892