@echo off
echo ========================================
echo School Management System - Setup Guide
echo ========================================
echo.
echo JAVA VERSION:
java -version
echo.
echo ========================================
echo JAVA_HOME is set to: %JAVA_HOME%
echo ========================================
echo.
echo TO RUN THIS PROJECT, YOU NEED MAVEN:
echo.
echo Option 1 - Install Maven manually:
echo   1. Download from: https://maven.apache.org/download.cgi
echo   2. Extract to C:\apache-maven
echo   3. Add C:\apache-maven\bin to PATH
echo   4. Restart PowerShell and run: mvn clean install
echo.
echo Option 2 - Use IntelliJ IDEA Community Edition (FREE):
echo   1. Download from: https://www.jetbrains.com/idea/download/
echo   2. Open this project folder in IntelliJ
echo   3. IntelliJ will detect it as a Maven project
echo   4. Click the green Run button
echo.
echo Option 3 - Use Visual Studio Code:
echo   1. Install VS Code
echo   2. Install "Extension Pack for Java" extension
echo   3. Install "Spring Boot Extension Pack"
echo   4. Open this folder and run from VS Code
echo.
echo ========================================
echo QUICK MAVEN INSTALL (if you have internet):
echo ========================================
echo.
echo Run this in PowerShell AS ADMINISTRATOR:
echo.
echo Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
echo iwr -useb get.scoop.sh ^| iex
echo scoop install maven
echo.
echo ========================================
echo.
echo After installing Maven, run:
echo   mvn clean install
echo   mvn spring-boot:run
echo.
echo Or double-click: run_application.bat
echo.
pause
