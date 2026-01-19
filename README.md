# 🎓 Microservices Project - Complete Setup Guide

A comprehensive **Spring Boot microservices architecture** with Eureka service discovery, MySQL database, and complete local development setup.

## 📋 Project Structure

```
projet-microservice/
├── discovery/              # Eureka Server (Service Registry)
├── api-gateway/            # Spring Cloud Gateway
├── department/             # Department Service (REST API)
├── student/                # Student Service (REST API)
├── enrollment/             # Enrollment Service (CQRS + Feign Client)
├── docker-compose.yml      # MySQL Database Configuration
├── start.ps1               # Startup Script (PowerShell)
├── stop.ps1                # Shutdown Script (PowerShell)
└── README.md               # This file
```

## 🚀 Quick Start

### Prerequisites

- **Windows OS** with PowerShell 5.1+
- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven** (Optional - uses bundled `mvnw.cmd`)
- **Docker Desktop** ([Download](https://www.docker.com/products/docker-desktop))
- **Git** (for version control)

### Verify Prerequisites

```powershell
# Check Java
java -version

# Check Docker
docker --version
docker compose version

# Check Maven (optional)
mvn --version
```

### One-Command Startup

```powershell
# From project root directory
cd C:\Users\MSI\projet-microservice
.\start.ps1
```

That's it! The script will:
1. ✓ Verify all prerequisites
2. ✓ Start MySQL database in Docker
3. ✓ Start Eureka Discovery Server
4. ✓ Start all microservices in correct order
5. ✓ Monitor health of each service
6. ✓ Display dashboard URLs

### Expected Output

```
======================================================================
Starting Microservices Project...
======================================================================

[0/5] Checking prerequisites...
✓ Docker installed: Docker version 25.0.0
✓ Docker Compose installed
✓ Maven installed
✓ Java 17+ installed

[1/5] Setting up MySQL Database...
✓ MySQL is healthy and ready

[2/5] Starting Discovery Service (Eureka)...
✓ Eureka Server is ready at http://localhost:8761

[3/5] Starting Department Service...
✓ Department Service is ready at http://localhost:8081

[4/5] Starting Student Service...
✓ Student Service is ready at http://localhost:8083

[5/5] Starting API Gateway...
✓ API Gateway is ready at http://localhost:8080

✅ Startup complete!
```

## 📊 Services Overview

### 1. **Eureka Discovery Server** (Port 8761)
- Service registry for dynamic discovery
- Dashboard: http://localhost:8761
- All services register automatically

### 2. **API Gateway** (Port 8080)
- Central entry point for all requests
- Routes to microservices based on paths
- Swagger UI aggregation

**Routes:**
```
/department/** → Department Service
/student/**    → Student Service  
/enrollment/** → Enrollment Service
```

### 3. **Department Service** (Port 8081)
- REST API for department management
- MySQL database persistence
- Swagger UI: http://localhost:8081/swagger-ui.html
- Health: http://localhost:8081/actuator/health

### 4. **Student Service** (Port 8083)
- REST API for student management
- MySQL database persistence
- Swagger UI: http://localhost:8083/swagger-ui.html
- Health: http://localhost:8083/actuator/health

### 5. **Enrollment Service** (Port 8082)
- CQRS pattern with Axon Framework
- Feign client for inter-service communication
- Circuit breaker with Resilience4j
- MySQL database persistence
- Swagger UI: http://localhost:8082/swagger-ui.html
- Health: http://localhost:8082/actuator/health

### 6. **MySQL Database** (Port 3307)
- Docker container: `student-microservice-mysql`
- Username: `root`
- Password: `root123`
- Database: `studentdb`

## 🛑 Shutdown

```powershell
.\stop.ps1
```

This will:
- Gracefully stop all Spring Boot services
- Stop MySQL Docker container
- Clean up resources
- Verify all ports are free

## 📈 Monitoring & Debugging

### View Eureka Dashboard
```powershell
Start-Process "http://localhost:8761"
```

### View Service Health
```powershell
# Department Service
Invoke-WebRequest http://localhost:8081/actuator/health | ConvertTo-Json

# Student Service
Invoke-WebRequest http://localhost:8083/actuator/health | ConvertTo-Json

# Enrollment Service
Invoke-WebRequest http://localhost:8082/actuator/health | ConvertTo-Json
```

### View MySQL Logs
```powershell
docker logs student-microservice-mysql

# Or follow logs in real-time
docker logs -f student-microservice-mysql
```

### View Service Logs
Each service runs in its own command window, so you can see real-time logs for each service.

### Database Access
```powershell
# Connect to MySQL container
docker exec -it student-microservice-mysql mysql -uroot -proot123 -e "USE studentdb; SHOW TABLES;"
```

## 📝 API Examples

### Using Gateway (Preferred)
```powershell
# Get all departments
curl http://localhost:8080/department/api/departments

# Get all students
curl http://localhost:8080/student/api/students

# Get enrollments
curl http://localhost:8080/enrollment/api/enrollments
```

### Direct Service Calls (for testing)
```powershell
# Department Service directly
curl http://localhost:8081/api/departments

# Student Service directly
curl http://localhost:8083/api/students

# Enrollment Service directly
curl http://localhost:8082/api/enrollments
```

## 🔧 Configuration Details

### Application Properties

Each service has its own `application.properties`:

**Department Service** (`department/src/main/resources/application.properties`)
```properties
spring.application.name=department-service
spring.datasource.url=jdbc:mysql://localhost:3307/studentdb
spring.datasource.username=root
spring.datasource.password=root123
server.port=8081
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

**Student Service** (`student/src/main/resources/application.properties`)
```properties
spring.application.name=student-service
spring.datasource.url=jdbc:mysql://localhost:3307/studentdb
spring.datasource.username=root
spring.datasource.password=root123
server.port=8083
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

**Enrollment Service** (`enrollment/src/main/resources/application.properties`)
```properties
spring.application.name=enrollment-service
spring.datasource.url=jdbc:mysql://localhost:3307/studentdb
spring.datasource.username=root
spring.datasource.password=root123
server.port=8082
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Docker Configuration

**docker-compose.yml** automatically:
- Creates MySQL 8.0 container
- Sets up studentdb database
- Configures networking between services
- Initializes root credentials (root/root123)
- Exposes port 3307 for local access
- Mounts volume for data persistence

## 🐛 Troubleshooting

### Issue: "Docker not found"
**Solution:** Install Docker Desktop from https://www.docker.com/products/docker-desktop

### Issue: "Java version check failed"
**Solution:** Install Java 17+ from https://www.oracle.com/java/technologies/downloads/

### Issue: "Port already in use"
**Solution:** Either services are still running, or another app uses the port
```powershell
# Kill process using a specific port (e.g., 8761)
netstat -ano | findstr :8761
taskkill /PID <PID> /F
```

### Issue: "MySQL connection refused"
**Solution:** Wait for MySQL container to be fully healthy
```powershell
# Check container logs
docker logs student-microservice-mysql

# Restart container
docker restart student-microservice-mysql
```

### Issue: "Service not registering with Eureka"
**Solution:** Check service logs for connection errors and verify Eureka is running at http://localhost:8761

### Issue: "Maven build fails"
**Solution:** Clear Maven cache and rebuild
```powershell
cd <service-directory>
mvnw.cmd clean install -DskipTests
```

## 🔄 Service Startup Order

The `start.ps1` script automatically starts services in the correct order:

1. **Docker MySQL** (Database)
2. **Eureka Server** (Service Registry)
3. **Department Service** (Data Service)
4. **Student Service** (Data Service)
5. **Enrollment Service** (CQRS Service)
6. **API Gateway** (Entry Point)

Each service waits for dependencies before proceeding.

## 📚 Building Individual Services

To rebuild a single service with latest code:

```powershell
# Department Service
cd department
mvnw.cmd clean spring-boot:run

# Student Service
cd student
mvnw.cmd clean spring-boot:run

# Enrollment Service
cd enrollment
mvnw.cmd clean spring-boot:run
```

## 🧹 Cleanup

### Stop Everything
```powershell
.\stop.ps1
```

### Remove Database Volume
```powershell
docker compose down -v
```

### Remove All Containers
```powershell
docker compose down --rmi all
```

## 📊 Database Schema

The services use **JPA with Hibernate** for ORM:
- `spring.jpa.hibernate.ddl-auto=update` auto-creates tables
- Each service manages its own entities
- Database: `studentdb`

**Example Tables Created Automatically:**
- `department` table
- `student` table
- `enrollment` table
- `course` table
- `event_store` table (Axon Framework for Enrollment CQRS)

## 🌐 Network Configuration

Services communicate via:
- **Eureka Client-Server**: Service discovery
- **Feign Clients**: REST inter-service communication (Enrollment → Student)
- **Load Balancer**: Client-side load balancing
- **Circuit Breaker**: Resilience4j fault tolerance

## 📝 Logs & Monitoring

### Real-Time Logs
Each service runs in its own command window with real-time output.

### Actuator Endpoints
All services expose health and metrics:
```powershell
# Health check
http://localhost:8081/actuator/health

# All metrics
http://localhost:8081/actuator

# Specific metrics (Spring Boot Actuator)
http://localhost:8081/actuator/metrics
```

## 🚀 Advanced Usage

### Custom Start Parameters
```powershell
# Skip Docker startup (if MySQL already running)
.\start.ps1 -NoDocker

# Custom timeouts (in seconds)
.\start.ps1 -EurekaTimeout 45 -ServiceTimeout 35
```

### View Process Info
```powershell
Get-Content .microservices.json | ConvertFrom-Json
```

## 📦 Dependencies Overview

### All Services
- Spring Boot 3.x / 4.x
- Spring Cloud (Eureka, Feign, Gateway)
- Spring Data JPA
- MySQL Connector
- Lombok (code generation)

### Enrollment Service (CQRS)
- Axon Framework 4.9.3
- Resilience4j (Circuit Breaker)
- Guava (utilities)

### Gateway
- Spring Cloud Gateway
- SpringDoc OpenAPI (Swagger aggregation)

## 📞 Support & Documentation

- **Spring Boot:** https://spring.io/projects/spring-boot
- **Spring Cloud:** https://spring.io/projects/spring-cloud
- **Eureka:** https://github.com/Netflix/eureka
- **Axon Framework:** https://docs.axoniq.io/
- **Docker:** https://docs.docker.com/

## ✅ Verification Checklist

After startup, verify:

- [ ] Eureka Dashboard accessible at http://localhost:8761
- [ ] All 5 services registered in Eureka (discovery/api-gateway/department-service/student-service/enrollment-service)
- [ ] Department Service health: http://localhost:8081/actuator/health
- [ ] Student Service health: http://localhost:8083/actuator/health
- [ ] Enrollment Service health: http://localhost:8082/actuator/health
- [ ] MySQL container running: `docker ps | grep student-microservice-mysql`
- [ ] Database accessible: `docker exec -it student-microservice-mysql mysql -uroot -proot123 -e "SELECT 1"`
- [ ] API Gateway routes working: `curl http://localhost:8080/department/api/departments`

## 📄 License

This project follows your organization's standard license.

---

**Last Updated:** January 19, 2026
**Status:** ✅ Production Ready
