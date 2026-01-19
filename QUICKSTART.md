# ============================================================================
# QUICK START CHECKLIST
# ============================================================================

## Pre-Startup Checklist (Run Once)

- [ ] **Install Prerequisites**
  - [ ] Java 17+ (https://www.oracle.com/java/technologies/downloads/)
  - [ ] Docker Desktop (https://www.docker.com/products/docker-desktop)
  - [ ] PowerShell 5.1+ (built-in on Windows 10+)

- [ ] **Verify Installation**
  ```powershell
  java -version
  docker --version
  docker compose version
  ```

- [ ] **Clone/Download Project**
  ```powershell
  git clone <repository-url>
  cd projet-microservice
  ```

- [ ] **Grant Script Execution Permission** (if needed)
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
  ```

## Startup Procedure

### Option 1: Automated Startup (Recommended)
```powershell
cd C:\Users\MSI\projet-microservice
.\start.ps1
```

⏱️ **Expected Duration:** 3-5 minutes (first run may take longer for Maven builds)

### Option 2: Manual Startup (for debugging)

1. **Start MySQL**
   ```powershell
   docker compose up -d
   ```

2. **Start Eureka Server** (in new PowerShell window)
   ```powershell
   cd discovery
   mvnw.cmd clean spring-boot:run
   ```

3. **Start Department Service** (in new PowerShell window)
   ```powershell
   cd department
   mvnw.cmd clean spring-boot:run
   ```

4. **Start Student Service** (in new PowerShell window)
   ```powershell
   cd student
   mvnw.cmd clean spring-boot:run
   ```

5. **Start Enrollment Service** (in new PowerShell window)
   ```powershell
   cd enrollment
   mvnw.cmd clean spring-boot:run
   ```

6. **Start API Gateway** (in new PowerShell window)
   ```powershell
   cd api-gateway
   mvnw.cmd clean spring-boot:run
   ```

## Post-Startup Verification

### ✅ Service Health Checks

```powershell
# Check Eureka Dashboard
Start-Process "http://localhost:8761"

# Check Department Service
Invoke-WebRequest http://localhost:8081/actuator/health

# Check Student Service
Invoke-WebRequest http://localhost:8083/actuator/health

# Check Enrollment Service
Invoke-WebRequest http://localhost:8082/actuator/health

# Check API Gateway
Invoke-WebRequest http://localhost:8080
```

### ✅ Database Connection

```powershell
# Check MySQL is running
docker ps | grep student-microservice-mysql

# Connect to database
docker exec -it student-microservice-mysql mysql -uroot -proot123 studentdb -e "SHOW TABLES;"
```

### ✅ Service Registration

Visit http://localhost:8761 and verify all services appear:
- [ ] api-gateway (PORT 8080)
- [ ] department-service (PORT 8081)
- [ ] enrollment-service (PORT 8082)
- [ ] student-service (PORT 8083)

## Shutdown Procedure

### Automated Shutdown
```powershell
.\stop.ps1
```

### Manual Shutdown
1. Close each service command window (Ctrl+C)
2. Stop Docker: `docker compose down`
3. Verify ports are free: `netstat -ano | findstr :8761`

## Common Issues & Quick Fixes

| Issue | Quick Fix |
|-------|-----------|
| **"Docker not found"** | Install Docker Desktop |
| **"Java not found"** | Install Java 17+ |
| **"Port 8761 in use"** | Run `.\stop.ps1` or `taskkill /PID <PID> /F` |
| **"MySQL connection failed"** | Wait 10s and retry; check `docker logs student-microservice-mysql` |
| **"Service won't start"** | Check logs in service window; restart with `mvnw.cmd clean spring-boot:run` |
| **"Permission denied on script"** | Run: `Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser` |

## Useful Commands

```powershell
# View service logs in real-time
docker logs -f student-microservice-mysql

# List all running containers
docker ps

# Stop specific container
docker stop student-microservice-mysql

# Remove everything (caution!)
docker compose down -v

# Check which process uses a port
netstat -ano | findstr :8761
tasklist /fi "PID eq <PID>"
```

## API Testing

### Via Gateway (Recommended)
```powershell
# Department endpoints
curl http://localhost:8080/department/api/departments

# Student endpoints
curl http://localhost:8080/student/api/students

# Enrollment endpoints
curl http://localhost:8080/enrollment/api/enrollments
```

### Direct Service Access
```powershell
# Department Service (port 8081)
curl http://localhost:8081/api/departments

# Student Service (port 8083)
curl http://localhost:8083/api/students

# Enrollment Service (port 8082)
curl http://localhost:8082/api/enrollments
```

## Documentation Links

- [README.md](README.md) - Full setup guide
- [.env.example](.env.example) - Configuration documentation
- [Docker Compose Config](docker-compose.yml) - Database setup
- [Startup Script](start.ps1) - Automation script
- [Shutdown Script](stop.ps1) - Cleanup script

## Emergency Cleanup

If something goes wrong, use this nuclear option:

```powershell
# Kill all Java processes
Get-Process | Where-Object {$_.ProcessName -eq "java"} | Stop-Process -Force

# Stop all Docker containers
docker stop $(docker ps -q)

# Remove all stopped containers
docker container prune -f

# Start fresh (from project root)
docker compose down -v
docker compose up -d
```

## Environment Variables

If you need custom configuration, create a `.env` file based on `.env.example`:

```powershell
Copy-Item .env.example .env
# Edit .env with your custom values
```

## Next Steps

1. ✅ Run `.\start.ps1`
2. ✅ Wait for all services to start (3-5 minutes)
3. ✅ Visit http://localhost:8761
4. ✅ Test APIs via gateway (http://localhost:8080)
5. ✅ View logs in each service window
6. ✅ When done, run `.\stop.ps1`

---

**Stuck?** Check [README.md](README.md) for detailed troubleshooting section.
