@echo off
echo ========================================
echo Starting School Management System...
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven is not installed!
    echo.
    echo Please run SETUP_MAVEN.bat for installation instructions.
    echo.
    pause
    exit /b 1
)

echo [INFO] Maven found. Building and running application...
echo.

REM Build the project
echo [INFO] Building project...
call mvn clean install -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo [SUCCESS] Build completed!
echo ========================================
echo.
echo [INFO] Starting Spring Boot application...
echo.
echo Access the application at: http://localhost:8080
echo API endpoint: http://localhost:8080/api/auth/health
echo.
echo Default credentials:
echo   Admin: admin / Admin@123
echo.
echo Press Ctrl+C to stop the application
echo ========================================
echo.

REM Run the application
call mvn spring-boot:run
