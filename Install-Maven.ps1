# ========================================
# School Management System - Maven Installer
# ========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Maven Installation Script" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Check if Maven is already installed
$mavenInstalled = Get-Command mvn -ErrorAction SilentlyContinue
if ($mavenInstalled) {
    Write-Host "[SUCCESS] Maven is already installed!" -ForegroundColor Green
    Write-Host "Maven version:" -ForegroundColor Yellow
    mvn -version
    Write-Host "`nYou can now run: .\run_application.bat`n" -ForegroundColor Green
    Read-Host "Press Enter to exit"
    exit 0
}

Write-Host "[INFO] Maven is not installed. Installing...`n" -ForegroundColor Yellow

# Check if Scoop is installed
$scoopInstalled = Get-Command scoop -ErrorAction SilentlyContinue
if (-not $scoopInstalled) {
    Write-Host "[INFO] Scoop is not installed. Installing Scoop first..." -ForegroundColor Yellow
    Write-Host "[INFO] This requires PowerShell to run as Administrator`n" -ForegroundColor Yellow
    
    try {
        # Install Scoop
        Set-ExecutionPolicy RemoteSigned -Scope CurrentUser -Force
        Invoke-RestMethod get.scoop.sh | Invoke-Expression
        Write-Host "`n[SUCCESS] Scoop installed successfully!`n" -ForegroundColor Green
    }
    catch {
        Write-Host "`n[ERROR] Failed to install Scoop: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "`nMANUAL INSTALLATION REQUIRED:" -ForegroundColor Yellow
        Write-Host "1. Download Maven from: https://maven.apache.org/download.cgi" -ForegroundColor White
        Write-Host "2. Extract to C:\apache-maven" -ForegroundColor White
        Write-Host "3. Add C:\apache-maven\bin to your PATH" -ForegroundColor White
        Write-Host "4. Restart PowerShell and run: mvn -version`n" -ForegroundColor White
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Install Maven using Scoop
Write-Host "[INFO] Installing Maven using Scoop..." -ForegroundColor Yellow
try {
    scoop install maven
    Write-Host "`n[SUCCESS] Maven installed successfully!`n" -ForegroundColor Green
    Write-Host "Maven version:" -ForegroundColor Yellow
    mvn -version
    Write-Host "`n[INFO] You can now run the application:" -ForegroundColor Green
    Write-Host "  Option 1: .\run_application.bat" -ForegroundColor White
    Write-Host "  Option 2: mvn spring-boot:run`n" -ForegroundColor White
}
catch {
    Write-Host "`n[ERROR] Failed to install Maven: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "`nMANUAL INSTALLATION REQUIRED:" -ForegroundColor Yellow
    Write-Host "1. Download Maven from: https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host "2. Extract to C:\apache-maven" -ForegroundColor White
    Write-Host "3. Add C:\apache-maven\bin to your PATH`n" -ForegroundColor White
}

Read-Host "Press Enter to exit"
