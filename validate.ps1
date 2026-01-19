# ============================================================================
# HEALTH CHECK SCRIPT - validate.ps1
# Purpose: Verify all prerequisites and configurations before startup
# ============================================================================

param(
    [switch]$Verbose = $false
)

$Colors = @{
    Success = 'Green'
    Error = 'Red'
    Warning = 'Yellow'
    Info = 'Cyan'
    Step = 'Magenta'
}

$totalChecks = 0
$passedChecks = 0
$failedChecks = 0

function Write-Check {
    param(
        [string]$Message,
        [string]$Status,
        [string]$Details = ""
    )
    
    $totalChecks++
    
    if ($Status -eq "PASS") {
        $passedChecks++
        Write-Host "  ✓ $Message" -ForegroundColor $Colors['Success']
    }
    elseif ($Status -eq "FAIL") {
        $failedChecks++
        Write-Host "  ✗ $Message" -ForegroundColor $Colors['Error']
        if ($Details) {
            Write-Host "    → $Details" -ForegroundColor $Colors['Warning']
        }
    }
    else {
        Write-Host "  ⊘ $Message" -ForegroundColor $Colors['Warning']
        if ($Details) {
            Write-Host "    → $Details" -ForegroundColor $Colors['Info']
        }
    }
}

Write-Host "`n" + ("=" * 70) -ForegroundColor $Colors['Step']
Write-Host "MICROSERVICES PROJECT - HEALTH CHECK" -ForegroundColor $Colors['Step']
Write-Host ("=" * 70) -ForegroundColor $Colors['Step']

# ============================================================================
# Section 1: Environment Prerequisites
# ============================================================================
Write-Host "`n[1] ENVIRONMENT PREREQUISITES`n" -ForegroundColor $Colors['Step']

# Check PowerShell Version
try {
    $psVersion = $PSVersionTable.PSVersion.Major
    if ($psVersion -ge 5) {
        Write-Check "PowerShell Version" "PASS" "Version: $psVersion"
    }
    else {
        Write-Check "PowerShell Version" "FAIL" "Requires 5.1+, found: $psVersion"
    }
}
catch {
    Write-Check "PowerShell Version" "FAIL" "Unable to determine version"
}

# Check Java
try {
    $javaVersion = java -version 2>&1
    if ($javaVersion) {
        $match = $javaVersion[0] -match '(\d+\.\d+|\d+)'
        if ($match) {
            Write-Check "Java Installation" "PASS" "Found: $($matches[1])"
        }
        else {
            Write-Check "Java Installation" "PASS" "Installed"
        }
    }
    else {
        Write-Check "Java Installation" "FAIL" "java command not found"
    }
}
catch {
    Write-Check "Java Installation" "FAIL" "Error: $_"
}

# Check Docker
try {
    $dockerVersion = docker --version 2>$null
    if ($dockerVersion) {
        Write-Check "Docker Installation" "PASS" "$dockerVersion"
    }
    else {
        Write-Check "Docker Installation" "FAIL" "docker command not found"
    }
}
catch {
    Write-Check "Docker Installation" "FAIL" "Error running docker"
}

# Check Docker Compose
try {
    $composeVersion = docker compose version 2>$null
    if ($composeVersion) {
        Write-Check "Docker Compose" "PASS" "Installed"
    }
    else {
        Write-Check "Docker Compose" "FAIL" "docker compose command not found"
    }
}
catch {
    Write-Check "Docker Compose" "FAIL" "Error running docker compose"
}

# Check Maven (optional)
try {
    $mvnVersion = mvn --version 2>$null
    if ($mvnVersion) {
        Write-Check "Maven Installation (Optional)" "PASS" "System Maven installed"
    }
    else {
        Write-Check "Maven Installation (Optional)" "SKIP" "Will use mvnw wrapper"
    }
}
catch {
    Write-Check "Maven Installation (Optional)" "SKIP" "Will use mvnw wrapper"
}

# ============================================================================
# Section 2: Project Files
# ============================================================================
Write-Host "`n[2] PROJECT FILES`n" -ForegroundColor $Colors['Step']

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

$requiredFiles = @(
    "docker-compose.yml",
    "start.ps1",
    "stop.ps1",
    "README.md",
    "QUICKSTART.md"
)

foreach ($file in $requiredFiles) {
    $filePath = Join-Path $projectRoot $file
    if (Test-Path $filePath) {
        $fileSize = (Get-Item $filePath).Length
        Write-Check "File: $file" "PASS" "$($fileSize / 1KB)KB"
    }
    else {
        Write-Check "File: $file" "FAIL" "Not found"
    }
}

# ============================================================================
# Section 3: Service Directories
# ============================================================================
Write-Host "`n[3] SERVICE DIRECTORIES`n" -ForegroundColor $Colors['Step']

$services = @("discovery", "api-gateway", "department", "student", "enrollment")

foreach ($service in $services) {
    $servicePath = Join-Path $projectRoot $service
    $pomPath = Join-Path $servicePath "pom.xml"
    $mvnwPath = Join-Path $servicePath "mvnw.cmd"
    
    if (Test-Path $servicePath) {
        $status = if ((Test-Path $pomPath) -and (Test-Path $mvnwPath)) { "PASS" } else { "FAIL" }
        Write-Check "Service: $service" $status
    }
    else {
        Write-Check "Service: $service" "FAIL" "Directory not found"
    }
}

# ============================================================================
# Section 4: Port Availability
# ============================================================================
Write-Host "`n[4] PORT AVAILABILITY`n" -ForegroundColor $Colors['Step']

$ports = @{
    "8080" = "API Gateway"
    "8081" = "Department Service"
    "8082" = "Enrollment Service"
    "8083" = "Student Service"
    "8761" = "Eureka Server"
    "3307" = "MySQL"
}

foreach ($port in $ports.GetEnumerator()) {
    try {
        $connection = New-Object System.Net.Sockets.TcpClient
        $asyncResult = $connection.BeginConnect("127.0.0.1", [int]$port.Key, $null, $null)
        
        if ($asyncResult.AsyncWaitHandle.WaitOne(500)) {
            if ($connection.Connected) {
                Write-Check "Port $($port.Key) - $($port.Value)" "FAIL" "Port already in use!"
            }
            else {
                Write-Check "Port $($port.Key) - $($port.Value)" "PASS" "Available"
            }
        }
        else {
            Write-Check "Port $($port.Key) - $($port.Value)" "PASS" "Available"
        }
        $connection.Close()
    }
    catch {
        Write-Check "Port $($port.Key) - $($port.Value)" "PASS" "Available"
    }
}

# ============================================================================
# Section 5: Docker Configuration
# ============================================================================
Write-Host "`n[5] DOCKER CONFIGURATION`n" -ForegroundColor $Colors['Step']

# Check if Docker is running
try {
    $dockerInfo = docker info 2>$null
    if ($dockerInfo) {
        Write-Check "Docker Daemon Running" "PASS" "Connected"
    }
    else {
        Write-Check "Docker Daemon Running" "FAIL" "Unable to connect to Docker daemon"
    }
}
catch {
    Write-Check "Docker Daemon Running" "FAIL" "Docker not running or not installed"
}

# Check for existing MySQL container
try {
    $existingContainer = docker ps -a --filter "name=student-microservice-mysql" --quiet 2>$null
    if ($existingContainer) {
        $isRunning = docker ps --filter "name=student-microservice-mysql" --quiet 2>$null
        if ($isRunning) {
            Write-Check "MySQL Container" "PASS" "Already running (will reuse)"
        }
        else {
            Write-Check "MySQL Container" "SKIP" "Exists but stopped (will restart)"
        }
    }
    else {
        Write-Check "MySQL Container" "SKIP" "Will be created on first startup"
    }
}
catch {
    Write-Check "MySQL Container" "SKIP" "Will create on first startup"
}

# ============================================================================
# Section 6: Disk Space
# ============================================================================
Write-Host "`n[6] DISK SPACE`n" -ForegroundColor $Colors['Step']

try {
    $drive = Get-PSDrive C
    $freeGB = [math]::Round($drive.Free / 1GB, 2)
    
    if ($freeGB -gt 5) {
        Write-Check "Free Disk Space" "PASS" "$($freeGB)GB available"
    }
    elseif ($freeGB -gt 2) {
        Write-Check "Free Disk Space" "SKIP" "$($freeGB)GB available (minimum 2GB)"
    }
    else {
        Write-Check "Free Disk Space" "FAIL" "Only $($freeGB)GB available (need 2GB+)"
    }
}
catch {
    Write-Check "Free Disk Space" "SKIP" "Unable to determine"
}

# ============================================================================
# Section 7: Configuration Files
# ============================================================================
Write-Host "`n[7] CONFIGURATION FILES`n" -ForegroundColor $Colors['Step']

$configFiles = @(
    "discovery\src\main\resources\application.properties",
    "api-gateway\src\main\resources\application.properties",
    "department\src\main\resources\application.properties",
    "student\src\main\resources\application.properties",
    "enrollment\src\main\resources\application.properties"
)

foreach ($configFile in $configFiles) {
    $configPath = Join-Path $projectRoot $configFile
    if (Test-Path $configPath) {
        $content = Get-Content $configPath -Raw
        
        # Check for important properties
        $hasAppName = $content -match 'spring\.application\.name'
        $hasPort = $content -match 'server\.port'
        
        $status = if ($hasAppName -and $hasPort) { "PASS" } else { "FAIL" }
        Write-Check "Config: $(Split-Path $configFile -Leaf)" $status
    }
    else {
        Write-Check "Config: $(Split-Path $configFile -Leaf)" "FAIL" "File not found"
    }
}

# ============================================================================
# Summary
# ============================================================================
Write-Host "`n" + ("=" * 70) -ForegroundColor $Colors['Step']

$totalErrors = $failedChecks
$totalWarnings = 0

Write-Host "`nCHECK SUMMARY:" -ForegroundColor $Colors['Step']
Write-Host "  Total Checks: $totalChecks"
Write-Host "  ✓ Passed: $passedChecks" -ForegroundColor $Colors['Success']
Write-Host "  ✗ Failed: $failedChecks" -ForegroundColor $(if ($failedChecks -gt 0) { $Colors['Error'] } else { $Colors['Success'] })
Write-Host ""

if ($failedChecks -eq 0) {
    Write-Host "✅ ALL CHECKS PASSED - System is ready!" -ForegroundColor $Colors['Success']
    Write-Host "`nYou can now run: .\start.ps1" -ForegroundColor $Colors['Info']
    exit 0
}
else {
    Write-Host "❌ SOME CHECKS FAILED - Please fix issues above" -ForegroundColor $Colors['Error']
    Write-Host "`nCommon fixes:" -ForegroundColor $Colors['Warning']
    
    if ($failedChecks -match "Java") {
        Write-Host "  • Install Java 17+: https://www.oracle.com/java/technologies/downloads/" -ForegroundColor $Colors['Warning']
    }
    
    if ($failedChecks -match "Docker") {
        Write-Host "  • Install Docker Desktop: https://www.docker.com/products/docker-desktop" -ForegroundColor $Colors['Warning']
    }
    
    if ($failedChecks -match "Port") {
        Write-Host "  • Kill processes using ports: .\stop.ps1" -ForegroundColor $Colors['Warning']
    }
    
    Write-Host ""
    exit 1
}
