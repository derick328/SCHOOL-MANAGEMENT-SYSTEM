# School Management System - Frontend Guide

## 🎨 Overview
The SMS frontend consists of modern, responsive web pages with role-based dashboards for different user types.

## 📁 File Structure

```
src/main/resources/static/
├── css/
│   └── styles.css          # Global styles with CSS variables
├── js/
│   └── app.js              # Shared utilities and authentication
├── home.html               # Landing page
├── login.html              # Login page
├── admin-dashboard.html    # Administrator dashboard
├── teacher-dashboard.html  # Teacher portal
├── student-dashboard.html  # Student portal
└── parent-dashboard.html   # Parent portal
```

## 🚀 Quick Start

### Access the Application

1. **Home Page**: http://localhost:8080/
2. **Login Page**: http://localhost:8080/login.html

### Demo Accounts

| Role    | Username      | Password     |
|---------|---------------|--------------|
| Admin   | admin         | Admin@123    |
| Teacher | teacher_demo  | Teacher@123  |
| Student | student_demo  | Student@123  |

## 🎯 Features by Role

### 👨‍💼 Admin Dashboard
- **Dashboard**: Overview with statistics (students, teachers, classes, subjects)
- **Student Management**: CRUD operations for students
- **Teacher Management**: Manage teacher accounts and assignments
- **Class Management**: Create and manage classes
- **Subject Management**: Configure subjects and curricula
- **Attendance**: View attendance reports
- **Exams & Results**: Manage examinations and results
- **Fee Management**: Track fee payments
- **User Management**: Manage all system users

### 👨‍🏫 Teacher Dashboard
- **Dashboard**: Overview of classes and students
- **My Classes**: View assigned classes
- **Students**: View and manage student information
- **Attendance**: Mark and track attendance
- **Grades & Marks**: Enter and manage grades
- **Schedule**: View teaching schedule
- **Assignments**: Create and manage assignments

### 👨‍🎓 Student Dashboard
- **Dashboard**: Personal overview with statistics
- **My Profile**: View and edit personal information
- **Class Schedule**: View class timetable
- **My Attendance**: Track attendance records
- **Grades & Results**: View exam results and grades
- **Assignments**: View and submit assignments
- **Fee Details**: Check fee payment status

### 👪 Parent Dashboard
- **Dashboard**: Overview of children's performance
- **My Children**: Manage linked children
- **Attendance**: View children's attendance
- **Academic Performance**: Track grades and results
- **Fee Payments**: View and manage fee payments
- **Teachers**: Contact children's teachers
- **Announcements**: View school announcements

## 🎨 Design System

### Color Palette
```css
--primary: #2563eb    /* Blue - Primary actions */
--secondary: #64748b  /* Gray - Secondary text */
--success: #10b981    /* Green - Success states */
--danger: #ef4444     /* Red - Errors/Delete */
--warning: #f59e0b    /* Orange - Warnings */
--info: #06b6d4       /* Cyan - Info messages */
```

### Components

#### Cards
Stats and content cards with hover effects:
```html
<div class="card">
    <div class="card-header">
        <span class="card-title">Title</span>
        <div class="card-icon icon-primary">🎯</div>
    </div>
    <div class="card-value">Value</div>
    <div class="card-label">Label</div>
</div>
```

#### Buttons
```html
<button class="btn btn-primary">Primary Action</button>
<button class="btn btn-success">Success</button>
<button class="btn btn-danger">Delete</button>
<button class="btn btn-outline">Secondary</button>
```

#### Badges
```html
<span class="badge badge-success">Active</span>
<span class="badge badge-warning">Pending</span>
<span class="badge badge-danger">Inactive</span>
```

## 🔐 Authentication Flow

### Login Process
1. User enters credentials on `/login.html`
2. JavaScript calls `/api/auth/login` endpoint
3. JWT token stored in `localStorage`
4. User redirected to role-specific dashboard
5. Token sent in `Authorization: Bearer <token>` header for API calls

### Auto-Redirect
- If logged in, home/login pages redirect to dashboard
- If not logged in, dashboard pages redirect to login
- Role-based routing ensures users access correct dashboard

## 📱 Responsive Design

All pages are fully responsive with breakpoints:
- **Desktop**: > 768px (sidebar + content)
- **Tablet/Mobile**: < 768px (stacked layout)

## 🛠️ JavaScript API

### Authentication Utilities (`app.js`)

```javascript
// Get stored token
Auth.getToken()

// Get current user
Auth.getUser()

// Check if authenticated
Auth.isAuthenticated()

// Get user role
Auth.getRole()

// Logout
API.logout()

// Make authenticated API call
API.request('/endpoint', { method: 'GET' })
```

### UI Utilities

```javascript
// Show alert message
UI.showAlert('Success message', 'success')
UI.showAlert('Error message', 'error')

// Format dates
UI.formatDate(date)
UI.formatTime(date)

// Show loading spinner
UI.showLoading(element)
```

## 🔄 Data Flow

```
User Action → JavaScript Event → API Call → Backend → Database
                ↓                   ↓
           Update UI ← Response ←────┘
```

## 🚧 Module Status

### ✅ Completed
- Authentication system
- Role-based dashboards
- Responsive layouts
- Token management
- User profile display

### 🔨 In Progress
- Student CRUD operations
- Teacher management
- Attendance marking

### 📋 Planned
- Class management
- Subject management
- Exam management
- Fee management
- Notifications
- Real-time updates

## 🎯 Next Steps

1. **Student Module**: Complete backend + frontend for student CRUD
2. **Teacher Module**: Implement teacher management
3. **Attendance System**: Real-time attendance marking
4. **Grade Entry**: Marks and result management
5. **File Uploads**: Handle profile pictures and documents
6. **Charts**: Add data visualization (Chart.js)
7. **Real-time**: WebSocket for live updates

## 📝 Best Practices

### Security
- Never expose JWT tokens in URLs
- Always validate user input
- Use HTTPS in production
- Implement CSRF tokens for state-changing operations

### Performance
- Lazy load images
- Minimize API calls
- Cache frequently accessed data
- Use pagination for large datasets

### UX
- Show loading states
- Provide clear error messages
- Confirm destructive actions
- Auto-save where possible

## 🐛 Debugging

### Common Issues

**Token Expired**
- Implement refresh token logic
- Auto-redirect to login on 401

**CORS Errors**
- Verify SecurityConfig allows origin
- Check request headers

**Layout Issues**
- Clear browser cache
- Check browser console for CSS errors

### Browser DevTools
- **Console**: Check for JavaScript errors
- **Network**: Monitor API calls
- **Application**: Inspect localStorage tokens

## 📚 Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MDN Web Docs](https://developer.mozilla.org/)
- [JWT.io](https://jwt.io/)

## 🤝 Contributing

When adding new pages:
1. Use existing CSS classes for consistency
2. Include authentication check with `checkAuth()`
3. Add route to `SecurityConfig.java`
4. Test on multiple screen sizes
5. Handle loading and error states

---

**Version**: 1.0.0  
**Last Updated**: January 2026  
**Maintainer**: School Management System Team
