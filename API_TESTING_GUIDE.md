# API Testing Examples for School Management System

Base URL: http://localhost:8080

## 1. Register New User (Student)

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john.doe@student.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "1234567890",
    "role": "STUDENT",
    "gender": "MALE"
  }'
```

## 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "Admin@123",
    "rememberMe": true
  }'
```

**Save the accessToken from response for authenticated requests!**

## 3. Get Current User Profile

```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

## 4. Change Password

```bash
curl -X POST http://localhost:8080/api/auth/change-password \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "Admin@123",
    "newPassword": "NewSecurePass123!",
    "confirmPassword": "NewSecurePass123!"
  }'
```

## 5. Forgot Password

```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@school.com"
  }'
```

## 6. Reset Password (with token from email)

```bash
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "token": "RESET_TOKEN_FROM_EMAIL",
    "newPassword": "NewSecurePass123!",
    "confirmPassword": "NewSecurePass123!"
  }'
```

## 7. Refresh Access Token

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN_HERE"
  }'
```

## 8. Logout

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

## 9. Health Check

```bash
curl -X GET http://localhost:8080/api/auth/health
```

---

## Testing with Postman

### Setup Environment Variables
1. Create a new environment in Postman
2. Add variables:
   - `baseUrl`: http://localhost:8080
   - `accessToken`: (will be set automatically)
   - `refreshToken`: (will be set automatically)

### Auto-save tokens after login
In the Login request, add this to the "Tests" tab:

```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("accessToken", jsonData.data.accessToken);
    pm.environment.set("refreshToken", jsonData.data.refreshToken);
}
```

### Use token in headers
For authenticated requests, add header:
```
Authorization: Bearer {{accessToken}}
```

---

## Expected Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response data */ },
  "timestamp": "2026-01-10T12:00:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error message",
  "data": null,
  "timestamp": "2026-01-10T12:00:00"
}
```

### Validation Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "fieldName": "error message",
    "anotherField": "another error"
  },
  "timestamp": "2026-01-10T12:00:00"
}
```

---

## Testing Different Roles

### Admin Login
```json
{
  "usernameOrEmail": "admin",
  "password": "Admin@123"
}
```

### Teacher Login
```json
{
  "usernameOrEmail": "teacher_demo",
  "password": "Teacher@123"
}
```

### Student Login
```json
{
  "usernameOrEmail": "student_demo",
  "password": "Student@123"
}
```

---

## Common HTTP Status Codes

- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid input
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

---

## Security Testing

### Test Account Lockout
Make 5 failed login attempts to trigger lockout:

```bash
for i in {1..5}; do
  curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"usernameOrEmail": "admin", "password": "WrongPassword"}';
  echo "\nAttempt $i";
done
```

### Test Password Validation
Try weak passwords to see validation errors:

```json
{
  "username": "testuser",
  "password": "weak",
  "confirmPassword": "weak",
  ...
}
```

---

For more information, see README.md
