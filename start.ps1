# ============================================================================
# Microservices Startup Script - start.ps1
# Purpose: Start all microservices with proper ordering and health checks
# ============================================================================

param(
    [switch]$NoDocker = $false,
    [int]$EurekaTimeout = 30,
    [int]$ServiceTimeout = 25
)

# Colors for output
$Colors = @{
    Success = 'Green'
    Error = 'Red'
    Warning = 'Yellow'
    Info = 'Cyan'
    Step = 'Magenta'
}

function Write-Status {
    param(
        [string]$Message,
        [string]$Type = 'Info'
    )
    $color = $Colors[$Type]
    Write-Host $Message -ForegroundColor $color
}

function Write-Header {
    param([string]$Text)
    Write-Host "`n" + ("=" * 70) -ForegroundColor $Colors['Step']
    Write-Host $Text -ForegroundColor $Colors['Step']
    Write-Host ("=" * 70) -ForegroundColor $Colors['Step']
}

function Test-ServiceHealth {
    param(
        [string]$Url,
        [int]$Timeout = 30
    )
    
    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    $maxAttempts = $Timeout * 2
    $attempt = 0
    
    while ($stopwatch.Elapsed.TotalSeconds -lt $Timeout) {
        try {
            $response = Invoke-WebRequest -Uri $Url -ErrorAction SilentlyContinue -TimeoutSec 2
            if ($response.StatusCode -eq 200) {
                $stopwatch.Stop()
                return $true
            }
        }
        catch {
            # Service not ready yet
        }
        
        Start-Sleep -Milliseconds 500
        $attempt++
    }
    
    $stopwatch.Stop()
    return $false
}

function Invoke-Maven {
    param(
        [string]$Path,
        [string]$Goal = 'clean spring-boot:run'
    )
    
    $scriptBlock = {
        param($WorkingDirectory, $Goal)
        Set-Location $WorkingDirectory
        
        if (Test-Path "mvnw.cmd") {
            & ".\mvnw.cmd" $Goal.Split(' ')
        }
        else {
            & mvn $Goal.Split(' ')
        }
    }
    
    return $scriptBlock, @{WorkingDirectory = $Path; Goal = $Goal}
}

# Get the project root
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

Write-Header "Starting Microservices Project"

# ============================================================================
# Step 1: Check Prerequisites
# ============================================================================
Write-Status "[0/5] Checking prerequisites..." Step

# Check if Docker and Docker Compose are installed
if (-not $NoDocker) {
    $dockerInstalled = $null
    try {
        $dockerInstalled = docker --version 2>$null
        if ($dockerInstalled) {
            Write-Status "✓ Docker installed: $dockerInstalled" Success
        }
    }
    catch {
        Write-Status "✗ Docker not found. Install Docker Desktop for Windows." Error
        exit 1
    }
    
    $composeInstalled = $null
    try {
        $composeInstalled = docker compose version 2>$null
        if ($composeInstalled) {
            Write-Status "✓ Docker Compose installed" Success
        }
    }
    catch {
        Write-Status "✗ Docker Compose not found" Error
        exit 1
    }
}

# Check if Maven is installed
try {
    $mvnVersion = mvn --version 2>$null
    if ($mvnVersion) {
        Write-Status "✓ Maven installed" Success
    }
}
catch {
    Write-Status "✗ Maven not found. Install Maven or use the included mvnw wrapper." Error
    exit 1
}

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    if ($javaVersion) {
        Write-Status "✓ Java 17+ installed" Success
    }
}
catch {
    Write-Status "✗ Java not found. Please install Java 17 or later." Error
    exit 1
}

# ============================================================================
# Step 2: Start Docker MySQL Database
# ============================================================================
Write-Status "`n[1/5] Setting up MySQL Database..." Step

if (-not $NoDocker) {
    # Check if Docker container is already running
    $containerRunning = $(docker ps --filter "name=student-microservice-mysql" --quiet)
    
    if ($containerRunning) {
        Write-Status "✓ MySQL container already running" Success
    }
    else {
        # Check if container exists but is stopped
        $containerExists = $(docker ps -a --filter "name=student-microservice-mysql" --quiet)
        
        if ($containerExists) {
            Write-Status "Starting existing MySQL container..." Info
            docker start student-microservice-mysql | Out-Null
        }
        else {
            Write-Status "Starting MySQL container with Docker Compose..." Info
            docker compose -f docker-compose.yml up -d
            
            if ($LASTEXITCODE -ne 0) {
                Write-Status "✗ Failed to start Docker container" Error
                exit 1
            }
        }
    }
    
    # Wait for MySQL to be healthy
    Write-Status "Waiting for MySQL to be ready (up to 30 seconds)..." Info
    $mysqlReady = $false
    $maxAttempts = 60
    $attempt = 0
    
    while ($attempt -lt $maxAttempts) {
        try {
            $healthStatus = docker exec student-microservice-mysql mysqladmin ping -h localhost -uroot -proot123 2>&1
            if ($healthStatus -like "*mysqld is alive*") {
                $mysqlReady = $true
                break
            }
        }
        catch {
            # Container not ready yet
        }
        
        Start-Sleep -Milliseconds 500
        $attempt++
    }
    
    if ($mysqlReady) {
        Write-Status "✓ MySQL is healthy and ready" Success
    }
    else {
        Write-Status "✗ MySQL failed to become healthy within timeout" Error
        docker logs student-microservice-mysql
        exit 1
    }
}
else {
    Write-Status "⊘ Docker skipped (--NoDocker flag set)" Warning
}

# ============================================================================
# Step 3: Start Discovery Service (Eureka)
# ============================================================================
Write-Status "`n[2/5] Starting Discovery Service (Eureka)..." Step

$discoveryPath = Join-Path $projectRoot "discovery"
$discoveryProcess = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c cd `"$discoveryPath`" && mvnw.cmd clean spring-boot:run" `
    -PassThru `
    -NoNewWindow

Write-Status "Waiting for Eureka Server to start (up to $EurekaTimeout seconds)..." Info

$eurekaStarted = Test-ServiceHealth -Url "http://localhost:8761" -Timeout $EurekaTimeout

if ($eurekaStarted) {
    Write-Status "✓ Eureka Server is ready at http://localhost:8761" Success
}
else {
    Write-Status "✗ Eureka Server failed to start" Error
    $discoveryProcess | Stop-Process -Force -ErrorAction SilentlyContinue
    exit 1
}

# ============================================================================
# Step 4: Start Data Services (Department, Student, Enrollment)
# ============================================================================
Write-Status "`n[3/5] Starting Department Service..." Step

$departmentPath = Join-Path $projectRoot "department"
$departmentProcess = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c cd `"$departmentPath`" && mvnw.cmd clean spring-boot:run" `
    -PassThru `
    -NoNewWindow

Write-Status "Waiting for Department Service to start (up to $ServiceTimeout seconds)..." Info

$departmentStarted = Test-ServiceHealth -Url "http://localhost:8081/actuator/health" -Timeout $ServiceTimeout

if ($departmentStarted) {
    Write-Status "✓ Department Service is ready at http://localhost:8081" Success
}
else {
    Write-Status "⚠ Department Service took too long to start (continuing anyway)" Warning
}

Write-Status "`n[4/5] Starting Student Service..." Step

$studentPath = Join-Path $projectRoot "student"
$studentProcess = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c cd `"$studentPath`" && mvnw.cmd clean spring-boot:run" `
    -PassThru `
    -NoNewWindow

Write-Status "Waiting for Student Service to start (up to $ServiceTimeout seconds)..." Info

$studentStarted = Test-ServiceHealth -Url "http://localhost:8083/actuator/health" -Timeout $ServiceTimeout

if ($studentStarted) {
    Write-Status "✓ Student Service is ready at http://localhost:8083" Success
}
else {
    Write-Status "⚠ Student Service took too long to start (continuing anyway)" Warning
}

# ============================================================================
# Step 5: Start Gateway
# ============================================================================
Write-Status "`n[5/5] Starting API Gateway..." Step

$gatewayPath = Join-Path $projectRoot "api-gateway"
$gatewayProcess = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c cd `"$gatewayPath`" && mvnw.cmd clean spring-boot:run" `
    -PassThru `
    -NoNewWindow

Write-Status "Waiting for API Gateway to start (up to $ServiceTimeout seconds)..." Info

$gatewayStarted = Test-ServiceHealth -Url "http://localhost:8080" -Timeout $ServiceTimeout

if ($gatewayStarted) {
    Write-Status "✓ API Gateway is ready at http://localhost:8080" Success
}
else {
    Write-Status "⚠ API Gateway took too long to start (continuing anyway)" Warning
}

# Also start Enrollment Service
Write-Status "`nStarting Enrollment Service..." Step

$enrollmentPath = Join-Path $projectRoot "enrollment"
$enrollmentProcess = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c cd `"$enrollmentPath`" && mvnw.cmd clean spring-boot:run" `
    -PassThru `
    -NoNewWindow

Write-Status "Waiting for Enrollment Service to start (up to $ServiceTimeout seconds)..." Info

$enrollmentStarted = Test-ServiceHealth -Url "http://localhost:8082/actuator/health" -Timeout $ServiceTimeout

if ($enrollmentStarted) {
    Write-Status "✓ Enrollment Service is ready at http://localhost:8082" Success
}
else {
    Write-Status "⚠ Enrollment Service took too long to start (continuing anyway)" Warning
}

# ============================================================================
# Success Summary
# ============================================================================
Write-Header "SUCCESS! Services Started"

Write-Status "`nAll microservices are now starting/running!`n" Success

Write-Host "📊 Service Status:`n" -ForegroundColor Cyan

$services = @(
    @{Name = "Eureka Discovery"; URL = "http://localhost:8761"; Port = 8761},
    @{Name = "API Gateway"; URL = "http://localhost:8080"; Port = 8080},
    @{Name = "Department Service"; URL = "http://localhost:8081/actuator/health"; Port = 8081},
    @{Name = "Student Service"; URL = "http://localhost:8083/actuator/health"; Port = 8083},
    @{Name = "Enrollment Service"; URL = "http://localhost:8082/actuator/health"; Port = 8082}
)

foreach ($service in $services) {
    Write-Host "   ✓ $($service.Name)" -ForegroundColor Green
    Write-Host "     → $($service.URL)" -ForegroundColor Gray
    Write-Host ""
}

Write-Host "📁 Database:`n" -ForegroundColor Cyan
Write-Host "   ✓ MySQL Container: student-microservice-mysql" -ForegroundColor Green
Write-Host "     → Connection: localhost:3307" -ForegroundColor Gray
Write-Host "     → Database: studentdb" -ForegroundColor Gray
Write-Host "     → User: root | Password: root123" -ForegroundColor Gray
Write-Host ""

Write-Host "🛑 To stop services, run:" -ForegroundColor Yellow
Write-Host "   .\stop.ps1" -ForegroundColor White
Write-Host ""

Write-Host "📝 Useful Commands:" -ForegroundColor Cyan
Write-Host "   View Eureka Dashboard:" -ForegroundColor Gray
Write-Host "   → Start-Process 'http://localhost:8761'" -ForegroundColor White
Write-Host ""
Write-Host "   View MySQL logs:" -ForegroundColor Gray
Write-Host "   → docker logs student-microservice-mysql" -ForegroundColor White
Write-Host ""
Write-Host "   View Service logs (run in each service directory):" -ForegroundColor Gray
Write-Host "   → mvnw.cmd spring-boot:run" -ForegroundColor White
Write-Host ""

# Save process information for the stop script
$processInfo = @{
    DiscoveryPID = $discoveryProcess.Id
    DepartmentPID = $departmentProcess.Id
    StudentPID = $studentProcess.Id
    EnrollmentPID = $enrollmentProcess.Id
    GatewayPID = $gatewayProcess.Id
    Timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
}

$processInfo | ConvertTo-Json | Out-File (Join-Path $projectRoot ".microservices.json") -Force

Write-Host "✅ Startup complete! Open Eureka dashboard: http://localhost:8761" -ForegroundColor Green
Write-Host ""
