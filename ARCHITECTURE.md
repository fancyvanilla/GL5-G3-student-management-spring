# Microservices Architecture Documentation

## System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        CLIENT APPLICATIONS                             │
└────────────────────────┬─────────────────────────────────────────────┘
                         │
                         │ HTTP/REST
                         │
                    ┌────▼──────────┐
                    │  API Gateway  │
                    │   (Port 8080) │
                    │ Spring Cloud  │
                    │   Gateway     │
                    └──────┬────────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          │                │                │
      ┌───▼────────┐  ┌───▼─────────┐  ┌──▼────────────┐
      │ Department │  │   Student   │  │  Enrollment   │
      │ Service    │  │   Service   │  │  Service      │
      │ Port: 8081 │  │  Port: 8083 │  │  Port: 8082   │
      │            │  │             │  │               │
      │ REST API   │  │  REST API   │  │ CQRS + Event  │
      │ JPA/MySQL  │  │  JPA/MySQL  │  │ Sourcing      │
      │            │  │             │  │ Axon + Feign  │
      └──────┬─────┘  └──────┬──────┘  └───┬───────────┘
             │               │              │
             │      Feign    │              │
             │      (IPC)    │              │
             └───────────────┼──────────────┘
                             │
                             │ Service Discovery
                             │ Load Balancing
                         ┌───▼──────────┐
                         │   Eureka     │
                         │   Server     │
                         │ (Port: 8761) │
                         │   Registry   │
                         └───┬──────────┘
                             │
                             │ JDBC
                             │
                         ┌───▼──────────┐
                         │    MySQL     │
                         │  Container   │
                         │ (Port: 3307) │
                         │              │
                         │  Database    │
                         │  studentdb   │
                         └──────────────┘
```

## Service Communication Flow

### 1. Client Request to API Gateway
```
Client
  │
  └─→ GET http://localhost:8080/department/api/departments
       │
       └─→ API Gateway (8080)
            │ Recognizes: /department/**
            │ Routes to: department-service
            │
            └─→ Department Service (8081)
                 │ Looks up in Eureka: "department-service"
                 │ Gets: 127.0.0.1:8081
                 │
                 └─→ Service Handler
                      │
                      └─→ MySQL Query
                           │
                           └─→ Response JSON
```

### 2. Service-to-Service Communication (Enrollment → Student)
```
Enrollment Service
  │
  └─→ Feign Client
       │ @FeignClient("student-service")
       │
       └─→ Service Registry (Eureka)
            │ Lookup: "student-service"
            │ Get: 127.0.0.1:8083
            │
            └─→ Student Service
                 │
                 └─→ Response to Enrollment
```

### 3. Service Discovery Registration
```
Each Service (on startup)
  │
  └─→ Eureka Client Library
       │ Sends: Service Name + Host:Port
       │ Interval: Every 30 seconds (heartbeat)
       │
       └─→ Eureka Server (8761)
            │ Stores in Registry
            │ Makes available to other services
            │
            └─→ Dashboard: http://localhost:8761
```

## Port Mapping Overview

```
┌─────────────────────────────────────────────────────┐
│              External (Windows Host)                │
├─────────────────────────────────────────────────────┤
│  localhost:8080  → API Gateway                      │
│  localhost:8081  → Department Service               │
│  localhost:8082  → Enrollment Service               │
│  localhost:8083  → Student Service                  │
│  localhost:8761  → Eureka Dashboard                 │
│  localhost:3307  → MySQL Database                   │
└─────────────────────────────────────────────────────┘
          │                                  │
          │ Docker Network Bridge            │
          │                                  │
┌─────────────────────────────────────────────────────┐
│            Docker Container Network                 │
├─────────────────────────────────────────────────────┤
│  (Internal)                                         │
│  mysql:3306 ← MySQL                                │
│  (Java services access as localhost)                │
└─────────────────────────────────────────────────────┘
```

## Data Flow Architecture

### Department Service Request
```
Request Path:
1. Browser/Client sends GET /department/api/departments
2. API Gateway receives on port 8080
3. Gateway routes to department-service (discovers via Eureka)
4. Department Service (port 8081) receives request
5. Service calls Spring Data JPA repository
6. JPA generates SQL query
7. Query executes against MySQL (jdbc:mysql://localhost:3307/)
8. Database returns result set
9. Result mapped to Java entity objects
10. Converted to JSON
11. Sent back through gateway
12. Browser receives JSON response

Technologies Used:
├── HTTP/REST for external communication
├── Feign for inter-service REST calls
├── Spring Data JPA for database ORM
├── Hibernate for SQL generation
├── MySQL JDBC driver for database connection
└── Jackson for JSON serialization
```

## Eureka Service Registration Lifecycle

```
Service Instance Startup:
├── 1. Read application.properties
├── 2. Load Eureka configuration
│   ├── eureka.client.service-url.defaultZone=http://localhost:8761/eureka
│   ├── spring.application.name=department-service
│   └── server.port=8081
├── 3. Create Eureka Client
├── 4. Register with Eureka Server
│   └── POST http://localhost:8761/eureka/apps/department-service
├── 5. Eureka acknowledges registration
├── 6. Service is marked as UP in registry
├── 7. Send heartbeat every 30 seconds
│   └── PUT http://localhost:8761/eureka/apps/department-service/{instanceId}
└── 8. Service discoverable to clients

Graceful Shutdown:
├── 1. Service receives shutdown signal
├── 2. Deregister from Eureka
│   └── DELETE http://localhost:8761/eureka/apps/department-service/{instanceId}
├── 3. Wait for in-flight requests to complete
├── 4. Close database connections
├── 5. Terminate process
└── 6. Service removed from registry
```

## Database Schema Relationships

```
┌──────────────────────┐
│   DEPARTMENT         │
├──────────────────────┤
│ id (PK)              │
│ name                 │
│ description          │
└──────────────────────┘
         │
         │ 1:N
         │
         ├─────────────────────────────────────────┐
         │                                         │
         ▼                                         ▼
┌──────────────────────┐              ┌──────────────────────┐
│   STUDENT            │              │   COURSE             │
├──────────────────────┤              ├──────────────────────┤
│ id (PK)              │              │ id (PK)              │
│ name                 │              │ name                 │
│ email                │              │ code                 │
│ department_id (FK)   │              │ credits              │
└──────────────────────┘              └──────────────────────┘
         │                                    │
         │                                    │
         │ M:N (through Enrollment)           │
         └────────────────┬────────────────────┘
                          │
                          ▼
            ┌──────────────────────────┐
            │   ENROLLMENT             │
            ├──────────────────────────┤
            │ id (PK)                  │
            │ student_id (FK)          │
            │ course_id (FK)           │
            │ status (PENDING/ACTIVE)  │
            │ enrollment_date          │
            └──────────────────────────┘
```

## Request/Response Flow Example

### GET Department Request
```json
REQUEST:
GET /department/api/departments HTTP/1.1
Host: localhost:8080
Accept: application/json

ROUTING (API Gateway):
Path: /department/** → department-service
Strip Prefix: 1
Actual Call: GET /api/departments

SERVICE PROCESSING:
DepartmentController.getAllDepartments()
  ├── Call: departmentService.getAllDepartments()
  │   └── Call: departmentRepository.findAll()
  │       └── Execute: SELECT * FROM department
  │           └── Receive: List<Department>
  └── Convert to DepartmentDTO
      └── Return JSON

RESPONSE:
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "name": "Computer Science",
    "description": "CS Department"
  },
  {
    "id": 2,
    "name": "Mathematics",
    "description": "Math Department"
  }
]
```

## Load Balancing & Failover

### Spring Cloud LoadBalancer
```
Feign Client in Enrollment Service
  │
  └─→ Request: "http://student-service/api/students/1"
       │
       └─→ Spring Cloud LoadBalancer
            │ 1. Get service name: "student-service"
            │ 2. Query Eureka: What instances of "student-service" exist?
            │ 3. Receive: [
            │      { host: 127.0.0.1, port: 8083, status: UP },
            │      { host: 127.0.0.1, port: 8083, status: UP }  # If scaled
            │    ]
            │ 4. Apply load balancing strategy (Round-robin)
            │ 5. Select instance: 127.0.0.1:8083
            │ 6. Make HTTP request
            │
            └─→ Student Service (8083)
                 │
                 └─→ Response

Resilience4j Circuit Breaker (Enrollment Service):
Normal State:
  Call → Service → Response
           ↑
        Monitor: Success Rate ≥ 50%

Degraded State (after 50% failures):
  Call → Service → Timeout/Error
           ↑
        Open Circuit: Return cached/default response

Recovery State:
  Call → Test Service → If OK: Close Circuit
```

## Database Transaction Flow

```
Enrollment Service CQRS Pattern:

Command (Write):
EnrollStudent Command
  │
  ├─→ Axon Command Bus
  │   │
  │   └─→ StudentEnrolledCommandHandler
  │       │
  │       ├─→ Publish Event: StudentEnrolledEvent
  │       │   │
  │       │   └─→ Axon Event Store (MySQL table: event_store)
  │       │       │ Save event to database
  │       │       │
  │       │       └─→ JPA Transaction
  │       │           BEGIN
  │       │           INSERT INTO event_store (...)
  │       │           COMMIT
  │       │
  │       └─→ Update Enrollment View
  │           │ Update current state
  │           │
  │           └─→ UPDATE enrollment SET status = 'ACTIVE'
  │
  └─→ Return Command Result

Query (Read):
GetEnrollments Query
  │
  └─→ Axon Query Bus
      │
      └─→ EnrollmentQueryHandler
          │
          └─→ SELECT * FROM enrollment WHERE student_id = ?
              │
              └─→ Return Result
```

## Monitoring & Observability Points

```
Eureka Dashboard (8761):
├── All registered services
├── Instance status (UP/DOWN/STARTING)
├── Last heartbeat
└── Instance metadata

Actuator Endpoints (each service):
├── /actuator/health
│   ├── Overall status
│   ├── Database connectivity
│   ├── Disk space
│   └── Memory usage
├── /actuator/metrics
│   ├── JVM metrics
│   ├── HTTP request metrics
│   ├── Database connection pool
│   └── Cache metrics
└── /actuator/env
    └── Active properties

Application Logs:
├── Spring Boot startup logs
├── SQL query logs (show-sql=true)
├── Eureka client logs
├── HTTP request logs
└── Circuit breaker state changes
```

## Security Flow (Future Enhancement)

```
Current State (Development):
Client → API Gateway → Services → Database
(No authentication/authorization)

Future State (Production):
Client
  │
  ├─→ OAuth2/OpenID Provider (e.g., Keycloak)
  │   │ User login
  │   └─→ Issue JWT Token
  │
  └─→ API Gateway
      │ Validate JWT
      │ Extract user info
      │
      └─→ Services
          │ Check authorization
          │ Access control
          │
          └─→ Database
              └─→ Return authorized data only
```

## Deployment Ready Checklist

```
Infrastructure:
✅ Docker for MySQL containerization
✅ Spring Boot embedded servers (Tomcat)
✅ Service discovery (Eureka)

Code Quality:
✅ Separation of concerns (Services)
✅ Dependency injection (Spring)
✅ Transaction management (JPA)
✅ Exception handling (Controllers)

Operations:
✅ Health checks (Actuator)
✅ Graceful shutdown hooks
✅ Logging framework (SLF4J)
✅ Startup/shutdown scripts

Scalability:
✅ Stateless services
✅ Load balancer ready
✅ Database connection pooling
✅ Service discovery for horizontal scaling

Testing:
🔲 Unit tests
🔲 Integration tests
🔲 End-to-end tests
🔲 Load tests
```

---

**Last Updated:** January 19, 2026
