# 🚀 HOW TO RUN THE SCHOOL MANAGEMENT SYSTEM

## ✅ Your Current Setup
- ✅ Java 21 is installed
- ✅ JAVA_HOME is configured
- ❌ Maven needs to be installed

## 📋 Three Easy Ways to Run the Project

### **Option 1: Install Maven (Recommended)**

#### Quick Install with Scoop (Easiest)
```powershell
# Run PowerShell AS ADMINISTRATOR and execute:
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
iwr -useb get.scoop.sh | iex
scoop install maven

# Then restart PowerShell and run:
mvn clean install
mvn spring-boot:run
```

#### Manual Install
1. Download Maven from: https://maven.apache.org/download.cgi
   - Download: apache-maven-3.9.6-bin.zip
2. Extract to `C:\apache-maven`
3. Add to PATH:
   ```
   C:\apache-maven\bin
   ```
4. Restart PowerShell
5. Verify: `mvn -version`
6. Run: `mvn spring-boot:run`

### **Option 2: Use IntelliJ IDEA (Best for Development)**

**IntelliJ IDEA has built-in Maven!**

1. **Download IntelliJ IDEA Community Edition (FREE)**
   - https://www.jetbrains.com/idea/download/

2. **Open the Project**
   - File → Open
   - Select: `C:\Users\Derick Mhidze\SCHOOL-MANAGEMENT-SYSTEM`

3. **Run the Application**
   - IntelliJ will detect it as a Maven project automatically
   - Wait for dependencies to download (first time only)
   - Find `SchoolManagementSystemApplication.java`
   - Click the green ▶️ Run button
   - Or right-click → Run 'SchoolManagementSystemApplication'

4. **Access the Application**
   - http://localhost:8080
   - http://localhost:8080/api/auth/health

### **Option 3: Use VS Code**

1. **Install VS Code Extensions**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Maven for Java

2. **Open the Project**
   - File → Open Folder
   - Select project folder

3. **Run**
   - Press `F5` or click Run
   - Or use Command Palette (`Ctrl+Shift+P`) → "Spring Boot: Run"

## 🎯 After Running the Application

### Test the API

#### 1. Health Check
```bash
curl http://localhost:8080/api/auth/health
```

#### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
```

### Default Credentials
- **Admin**: `admin` / `Admin@123`
- **Teacher**: `teacher_demo` / `Teacher@123`
- **Student**: `student_demo` / `Student@123`

### Database Setup
Make sure PostgreSQL is running with:
- Database: `school_management_db`
- Username: `postgres`
- Password: `postgres`

Create database:
```sql
CREATE DATABASE school_management_db;
```

## 🐛 Troubleshooting

### "JAVA_HOME not found"
Already fixed! ✅ JAVA_HOME is set to: `C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot`

### "Maven not found"
Follow Option 1 or 2 above.

### "Database connection failed"
- Start PostgreSQL service
- Create database: `createdb school_management_db`
- Check credentials in `src/main/resources/application.yml`

### Port 8080 already in use
- Stop other applications using port 8080
- Or change port in `application.yml`:
  ```yaml
  server:
    port: 8081
  ```

## 💡 Quick Commands (After Maven is Installed)

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Build JAR
mvn package

# Run JAR
java -jar target/school-management-system-1.0.0.jar

# Skip tests
mvn clean install -DskipTests
```

## 📚 Documentation
- README.md - Full documentation
- API_TESTING_GUIDE.md - API examples
- QUICK_REFERENCE.txt - Quick reference card

## ✨ Recommendation

**For the best experience, use IntelliJ IDEA Community Edition:**
- ✅ Free
- ✅ Built-in Maven (no separate installation needed)
- ✅ Excellent Spring Boot support
- ✅ Database tools included
- ✅ Built-in REST client for testing APIs
- ✅ Git integration

Download: https://www.jetbrains.com/idea/download/

---

**Need help?** Check the documentation files or the console output for error messages.
