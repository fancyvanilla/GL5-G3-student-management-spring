# ============================================================================
# Microservices Shutdown Script - stop.ps1
# Purpose: Gracefully shut down all running microservices and Docker
# ============================================================================

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

# Get the project root
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

Write-Header "Stopping Microservices"

# ============================================================================
# Stop Spring Boot Services
# ============================================================================
Write-Status "`n[1/3] Stopping Spring Boot Services..." Step

$servicesToStop = @(
    @{Name = "Eureka Discovery (port 8761)"; Pattern = "*discovery*"},
    @{Name = "API Gateway (port 8080)"; Pattern = "*api-gateway*"},
    @{Name = "Department Service (port 8081)"; Pattern = "*department*"},
    @{Name = "Student Service (port 8083)"; Pattern = "*student*"},
    @{Name = "Enrollment Service (port 8082)"; Pattern = "*enrollment*"}
)

foreach ($service in $servicesToStop) {
    Write-Status "Stopping $($service.Name)..." Info
    
    $processes = Get-Process | Where-Object {$_.ProcessName -match "java|cmd"} | Where-Object {$_.CommandLine -match $service.Pattern}
    
    foreach ($process in $processes) {
        try {
            Stop-Process -Id $process.Id -Force -ErrorAction SilentlyContinue
            Write-Status "  ✓ Stopped process ID: $($process.Id)" Success
        }
        catch {
            # Process might have already exited
        }
    }
}

# More aggressive: Kill all Java processes (be careful with this!)
Write-Status "`nForcing cleanup of any remaining Spring Boot processes..." Warning
Get-Process | Where-Object {$_.ProcessName -eq "java"} | ForEach-Object {
    try {
        Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue
        Write-Status "  ✓ Force stopped Java process: $($_.Id)" Success
    }
    catch {
        # Already stopped
    }
}

# Kill any cmd.exe processes that were spawned by our script
Get-Process | Where-Object {$_.ProcessName -eq "cmd"} | ForEach-Object {
    $processDetails = $_.MainWindowTitle
    if ($processDetails -match "mvnw|spring-boot|microservice") {
        try {
            Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue
            Write-Status "  ✓ Stopped cmd.exe process: $($_.Id)" Success
        }
        catch {
            # Already stopped
        }
    }
}

Start-Sleep -Seconds 2

# ============================================================================
# Stop Docker Container
# ============================================================================
Write-Status "`n[2/3] Stopping Docker MySQL Container..." Step

try {
    $containerRunning = $(docker ps --filter "name=student-microservice-mysql" --quiet)
    
    if ($containerRunning) {
        Write-Status "Found running MySQL container: $containerRunning" Info
        docker stop student-microservice-mysql | Out-Null
        
        if ($LASTEXITCODE -eq 0) {
            Write-Status "✓ MySQL container stopped gracefully" Success
        }
        else {
            Write-Status "  ⚠ Container stop had issues, attempting force stop..." Warning
            docker kill student-microservice-mysql | Out-Null
            Write-Status "✓ MySQL container force stopped" Success
        }
    }
    else {
        Write-Status "✓ MySQL container not running" Info
    }
}
catch {
    Write-Status "✗ Error stopping Docker container: $_" Error
}

# ============================================================================
# Cleanup
# ============================================================================
Write-Status "`n[3/3] Cleaning up..." Step

# Remove process info file
$processInfoFile = Join-Path $projectRoot ".microservices.json"
if (Test-Path $processInfoFile) {
    Remove-Item $processInfoFile -Force -ErrorAction SilentlyContinue
    Write-Status "✓ Cleaned up process information file" Success
}

# ============================================================================
# Summary
# ============================================================================
Write-Header "Shutdown Complete"

Write-Status "`nAll services have been stopped.`n" Success

Write-Host "📊 Current Status:`n" -ForegroundColor Cyan

$ports = @(8761, 8080, 8081, 8083, 8082)
$allClosed = $true

foreach ($port in $ports) {
    try {
        $connection = New-Object System.Net.Sockets.TcpClient
        $asyncResult = $connection.BeginConnect("127.0.0.1", $port, $null, $null)
        $asyncResult.AsyncWaitHandle.WaitOne(1000) | Out-Null
        
        if ($connection.Connected) {
            Write-Host "   ⚠ Port $port still appears to be in use" -ForegroundColor Yellow
            $allClosed = $false
        }
        else {
            Write-Host "   ✓ Port $port is free" -ForegroundColor Green
        }
        
        $connection.Close()
    }
    catch {
        Write-Host "   ✓ Port $port is free" -ForegroundColor Green
    }
}

Write-Host ""

if ($allClosed) {
    Write-Host "✅ All ports are now free. System is clean!" -ForegroundColor Green
}
else {
    Write-Host "⚠️  Some services may still be shutting down. Wait a moment and try again if needed." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "💡 To start services again, run:" -ForegroundColor Cyan
Write-Host "   .\start.ps1" -ForegroundColor White
Write-Host ""

Write-Host "🗑️  To remove the MySQL container completely, run:" -ForegroundColor Cyan
Write-Host "   docker compose down -v" -ForegroundColor White
Write-Host ""
