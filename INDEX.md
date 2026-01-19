# 🎓 Microservices Project - Complete Documentation Index

Welcome to the complete microservices setup! This document guides you to the right resources for your needs.

## ⚡ Get Started in 60 Seconds

```powershell
# 1. Open PowerShell
# 2. Navigate to project
cd C:\Users\MSI\projet-microservice

# 3. Run startup script
.\start.ps1

# 4. Wait 3-5 minutes
# 5. Open Eureka dashboard
Start-Process "http://localhost:8761"
```

**That's it!** All services will be running.

---

## 📚 Documentation by Use Case

### "I just want to run it"
1. Read: [QUICKSTART.md](QUICKSTART.md) - **START HERE** (5 min read)
2. Run: `.\start.ps1`
3. Verify: http://localhost:8761

### "I want to understand the architecture"
1. Read: [ARCHITECTURE.md](ARCHITECTURE.md) - Detailed diagrams and flows
2. View: Service descriptions in [README.md](README.md#-services-overview)
3. Reference: Database schema in [ARCHITECTURE.md](ARCHITECTURE.md#database-schema-relationships)

### "I want comprehensive setup details"
1. Start with: [README.md](README.md) - Full documentation
2. Reference: [SETUP_SUMMARY.md](SETUP_SUMMARY.md) - Configuration details
3. Learn: [ARCHITECTURE.md](ARCHITECTURE.md) - How components interact

### "I need to verify my system is ready"
1. Run: `.\validate.ps1` - Health check script
2. Fixes: See command output for issues
3. Verify: All checks pass (green checkmarks)

### "Something isn't working"
1. Check: [README.md#-troubleshooting](README.md#-troubleshooting)
2. Run: `.\stop.ps1` then `.\validate.ps1`
3. Emergency: [QUICKSTART.md#emergency-cleanup](QUICKSTART.md#emergency-cleanup)

### "I want to modify/debug services"
1. Read: [SETUP_SUMMARY.md#-logs-and-debugging](SETUP_SUMMARY.md#-logs-and-debugging)
2. Reference: [README.md#-monitoring--debugging](README.md#-monitoring--debugging)
3. Manual start: See [QUICKSTART.md#option-2-manual-startup](QUICKSTART.md#option-2-manual-startup)

### "I need to understand the code"
1. Start: [ARCHITECTURE.md](ARCHITECTURE.md) - System design
2. Explore: Service directories (discovery/, api-gateway/, etc.)
3. Reference: Each service's `src/main/java/` and `application.properties`

### "I want API examples"
1. See: [README.md#-api-examples](README.md#-api-examples)
2. Test: Using curl or Postman
3. Gateway routes: [README.md#-services-overview](README.md#-services-overview)

---

## 📂 File Organization

### 🚀 Execution Scripts
| File | Purpose | When to Use |
|------|---------|------------|
| [start.ps1](start.ps1) | **Start everything** | `.\start.ps1` → Main startup |
| [stop.ps1](stop.ps1) | **Stop everything** | `.\stop.ps1` → Graceful shutdown |
| [validate.ps1](validate.ps1) | **Health check** | `.\validate.ps1` → Verify prerequisites |

### 📖 Documentation Files
| File | Purpose | Read Time |
|------|---------|-----------|
| [README.md](README.md) | **Main documentation** - Complete guide | 15 min |
| [QUICKSTART.md](QUICKSTART.md) | **Fast reference** - Checklists & commands | 5 min |
| [SETUP_SUMMARY.md](SETUP_SUMMARY.md) | **Deep dive** - Configuration details | 10 min |
| [ARCHITECTURE.md](ARCHITECTURE.md) | **System design** - Diagrams & flows | 10 min |
| [.env.example](.env.example) | **Config template** - All variables | Reference |
| **INDEX.md** (this file) | **Navigation guide** - Find what you need | 5 min |

### 🐳 Infrastructure Files
| File | Purpose |
|------|---------|
| [docker-compose.yml](docker-compose.yml) | MySQL database setup |

### 📦 Service Directories
```
discovery/          → Eureka Server (Service Registry)
api-gateway/        → Spring Cloud Gateway (Router)
department/         → Department Service (REST API)
student/            → Student Service (REST API)
enrollment/         → Enrollment Service (CQRS)
```

---

## 🎯 Quick Reference

### Service Ports
| Service | Port | URL |
|---------|------|-----|
| **Eureka** | 8761 | http://localhost:8761 |
| **Gateway** | 8080 | http://localhost:8080 |
| **Department** | 8081 | http://localhost:8081 |
| **Enrollment** | 8082 | http://localhost:8082 |
| **Student** | 8083 | http://localhost:8083 |
| **MySQL** | 3307 | localhost:3307 (from Windows) |

### Health Check Commands
```powershell
# Start services
.\start.ps1

# Stop services
.\stop.ps1

# Validate system
.\validate.ps1

# View Eureka dashboard
Start-Process "http://localhost:8761"

# Check Department health
curl http://localhost:8081/actuator/health

# Check Student health
curl http://localhost:8083/actuator/health

# Check Enrollment health
curl http://localhost:8082/actuator/health

# View MySQL logs
docker logs student-microservice-mysql
```

### Database Access
```powershell
# Connect to MySQL
docker exec -it student-microservice-mysql mysql -uroot -proot123 -e "USE studentdb; SHOW TABLES;"

# View logs
docker logs -f student-microservice-mysql

# Restart container
docker restart student-microservice-mysql

# Cleanup everything
docker compose down -v
```

---

## 🔍 Document Summaries

### README.md
**Complete reference documentation**
- Project structure overview
- Prerequisites & installation
- Quick start instructions
- Detailed service descriptions
- Configuration details
- API examples with curl
- Monitoring & debugging
- Comprehensive troubleshooting
- **Best for:** Full understanding, reference

### QUICKSTART.md
**Fast-track setup guide**
- Pre-startup checklist
- Step-by-step procedures
- Health check commands
- Common issues & fixes
- Useful commands
- Emergency cleanup
- **Best for:** Getting started quickly, checklists

### SETUP_SUMMARY.md
**Configuration & architecture details**
- What was created
- System architecture overview
- Service details table
- Configuration documentation
- Performance characteristics
- Security considerations
- Startup workflow
- Health monitoring info
- **Best for:** Understanding components, debugging

### ARCHITECTURE.md
**System design & technical flows**
- System architecture diagram
- Service communication flows
- Port mapping
- Data flow diagrams
- Service registration lifecycle
- Database schema
- Request/response examples
- Load balancing & failover
- Monitoring points
- **Best for:** Technical deep dive, design understanding

---

## 🚦 Common Tasks

### Task: Start Everything
```powershell
.\start.ps1
# Wait 3-5 minutes
# Open http://localhost:8761
```
📖 Reference: [QUICKSTART.md](QUICKSTART.md#startup-procedure)

### Task: Stop Everything
```powershell
.\stop.ps1
# Wait for cleanup
```
📖 Reference: [QUICKSTART.md](QUICKSTART.md#shutdown-procedure)

### Task: Verify Setup is Ready
```powershell
.\validate.ps1
# Check for green checkmarks
```
📖 Reference: [QUICKSTART.md](QUICKSTART.md#pre-startup-checklist)

### Task: Check Service Health
```powershell
# Visit dashboard
Start-Process "http://localhost:8761"

# Or check endpoints
curl http://localhost:8081/actuator/health
curl http://localhost:8083/actuator/health
```
📖 Reference: [README.md#-monitoring--debugging](README.md#-monitoring--debugging)

### Task: Test APIs
```powershell
# Via Gateway (recommended)
curl http://localhost:8080/department/api/departments

# Or direct to service
curl http://localhost:8081/api/departments
```
📖 Reference: [README.md#-api-examples](README.md#-api-examples)

### Task: View Logs
```powershell
# Service logs appear in command windows

# MySQL logs
docker logs student-microservice-mysql

# Follow MySQL logs
docker logs -f student-microservice-mysql
```
📖 Reference: [README.md#-logs--monitoring](README.md#-logs--monitoring)

### Task: Fix Port Conflict
```powershell
# Method 1: Stop all services
.\stop.ps1

# Method 2: Find and kill process
netstat -ano | findstr :8761
taskkill /PID <PID> /F
```
📖 Reference: [QUICKSTART.md#common-issues--quick-fixes](QUICKSTART.md#common-issues--quick-fixes)

### Task: Reset Everything
```powershell
# Stop services
.\stop.ps1

# Remove Docker data
docker compose down -v

# Restart fresh
.\start.ps1
```
📖 Reference: [QUICKSTART.md#emergency-cleanup](QUICKSTART.md#emergency-cleanup)

---

## 🔄 Workflow Examples

### Example 1: First-Time Setup
1. Read: [QUICKSTART.md](QUICKSTART.md) - 5 minutes
2. Run: `.\validate.ps1` - 1 minute
3. Run: `.\start.ps1` - 3-5 minutes
4. Verify: http://localhost:8761 - 1 minute
5. **Total: 10-15 minutes** ✅

### Example 2: Troubleshooting
1. Check: [README.md#-troubleshooting](README.md#-troubleshooting)
2. Run: `.\stop.ps1`
3. Run: `.\validate.ps1`
4. Read: Specific issue section
5. Run: Suggested fix
6. Run: `.\start.ps1` to verify

### Example 3: Understanding Architecture
1. Read: [ARCHITECTURE.md](ARCHITECTURE.md) - 10 minutes
2. View: Diagrams and flows
3. Read: [README.md#-services-overview](README.md#-services-overview) - 5 minutes
4. **Total: 15 minutes** ✅

### Example 4: API Testing
1. Run: `.\start.ps1` (wait for completion)
2. Read: [README.md#-api-examples](README.md#-api-examples)
3. Copy: curl command
4. Test: Run in PowerShell
5. View: Response in JSON

---

## 📞 Support Matrix

| Need | Best Resource | Time |
|------|---------------|------|
| Get started NOW | [QUICKSTART.md](QUICKSTART.md) | 5 min |
| Understand design | [ARCHITECTURE.md](ARCHITECTURE.md) | 10 min |
| Full reference | [README.md](README.md) | 15 min |
| Configuration help | [SETUP_SUMMARY.md](SETUP_SUMMARY.md) | 10 min |
| Fix a problem | [README.md#troubleshooting](README.md#-troubleshooting) | 5 min |
| Check prerequisites | [validate.ps1](validate.ps1) | 1 min |
| API documentation | [README.md#api-examples](README.md#-api-examples) | 5 min |

---

## 🎓 Learning Path

### For Beginners
1. **Start:** [QUICKSTART.md](QUICKSTART.md) - Get it running
2. **Understand:** [SETUP_SUMMARY.md](SETUP_SUMMARY.md) - What's happening
3. **Learn:** [ARCHITECTURE.md](ARCHITECTURE.md) - How it works
4. **Reference:** [README.md](README.md) - Complete details

### For Experienced Developers
1. **Scan:** [ARCHITECTURE.md](ARCHITECTURE.md) - Design overview
2. **Review:** [SETUP_SUMMARY.md](SETUP_SUMMARY.md) - Configuration
3. **Run:** `.\validate.ps1` then `.\start.ps1`
4. **Explore:** Code in service directories

### For DevOps/Operations
1. **Study:** [docker-compose.yml](docker-compose.yml) - Infrastructure
2. **Read:** [SETUP_SUMMARY.md](SETUP_SUMMARY.md#-startup-workflow) - Orchestration
3. **Scripts:** [start.ps1](start.ps1) and [stop.ps1](stop.ps1) - Automation
4. **Monitor:** [README.md#-monitoring--debugging](README.md#-monitoring--debugging) - Health checks

---

## ✅ Verification Checklist

After reading this document, you should know:

- [ ] Where to run startup script: `.\start.ps1`
- [ ] When to read each documentation file
- [ ] How to verify services are running: http://localhost:8761
- [ ] Where to find API examples: [README.md](README.md)
- [ ] How to troubleshoot issues: [README.md#troubleshooting](README.md#-troubleshooting)
- [ ] Services running on ports: 8080, 8081, 8082, 8083, 8761, 3307
- [ ] Database credentials: root / root123
- [ ] How to gracefully shutdown: `.\stop.ps1`
- [ ] How to validate system: `.\validate.ps1`

---

## 🔗 Quick Links

### Jump to Service Documentation
- **Eureka Discovery:** [README.md#1-eureka-discovery-server](README.md#1-eureka-discovery-server-port-8761)
- **API Gateway:** [README.md#2-api-gateway](README.md#2-api-gateway-port-8080)
- **Department Service:** [README.md#3-department-service](README.md#3-department-service-port-8081)
- **Student Service:** [README.md#4-student-service](README.md#4-student-service-port-8083)
- **Enrollment Service:** [README.md#5-enrollment-service](README.md#5-enrollment-service-port-8082)

### Jump to Setup Sections
- **Install Prerequisites:** [README.md#prerequisites](README.md#prerequisites)
- **Quick Start:** [README.md#one-command-startup](README.md#one-command-startup)
- **Configuration:** [SETUP_SUMMARY.md#-configuration-details](SETUP_SUMMARY.md#-configuration-details)
- **Architecture:** [ARCHITECTURE.md](ARCHITECTURE.md)

### Jump to Troubleshooting
- **Troubleshooting Guide:** [README.md#-troubleshooting](README.md#-troubleshooting)
- **Emergency Reset:** [QUICKSTART.md#emergency-cleanup](QUICKSTART.md#emergency-cleanup)
- **Common Issues:** [QUICKSTART.md#common-issues--quick-fixes](QUICKSTART.md#common-issues--quick-fixes)

---

## 📝 Pro Tips

1. **Always validate first:** Run `.\validate.ps1` before `.\start.ps1`
2. **Read QUICKSTART:** Even experienced devs benefit from checklists
3. **Check health dashboard:** http://localhost:8761 is your best friend
4. **Monitor real-time:** Each service shows logs in its command window
5. **Keep scripts close:** `start.ps1` and `stop.ps1` in project root
6. **Save this file:** Bookmark INDEX.md for future reference

---

## 🎉 Ready?

1. Open PowerShell
2. Navigate to: `C:\Users\MSI\projet-microservice`
3. Run: `.\validate.ps1` (takes 1 minute)
4. Run: `.\start.ps1` (takes 3-5 minutes)
5. Open: http://localhost:8761

**Boom! 🚀 You're running a complete microservices architecture!**

---

**Last Updated:** January 19, 2026  
**Status:** ✅ Production Ready  
**Questions?** See [README.md](README.md#-support--documentation)
