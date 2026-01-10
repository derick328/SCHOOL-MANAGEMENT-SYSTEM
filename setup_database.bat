@echo off
echo ===============================================
echo School Management System - Database Setup
echo ===============================================
echo.
echo This script will create the database for the School Management System.
echo.
echo PostgreSQL must be running (postgresql-x64-17 service).
echo.
pause

set PGBIN=C:\Program Files\PostgreSQL\17\bin
set PGUSER=postgres

echo.
echo Creating database 'school_management_db'...
echo.
echo You will be prompted for the PostgreSQL password.
echo.

"%PGBIN%\psql" -U %PGUSER% -c "CREATE DATABASE school_management_db;"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Database created successfully!
    echo.
    echo Now you can start the application with: mvn spring-boot:run
) else (
    echo.
    echo ✗ Error creating database.
    echo.
    echo If the database already exists, that's fine - you can proceed.
    echo If authentication failed, you need to update the password in application.yml
)

echo.
pause
