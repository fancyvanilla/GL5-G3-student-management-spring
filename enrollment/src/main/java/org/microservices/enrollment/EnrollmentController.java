package org.microservices.enrollment;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.microservices.enrollment.command.CreateEnrollmentCommand;
import org.microservices.enrollment.query.GetEnrollmentByIdQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
public class EnrollmentController {
    IEnrollment enrollmentService;
    CommandGateway commandGateway;
    QueryGateway queryGateway;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Project is healthy");
    }

    @GetMapping("/getAllEnrollment")
    public List<Enrollment> getAllEnrollment() { return enrollmentService.getAllEnrollments(); }

    @GetMapping("/getEnrollment/{id}")
    public EnrollmentResponse getEnrollment(@PathVariable Long id) { return enrollmentService.getEnrollmentById(id); }

    @PostMapping("/createEnrollment")
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) { return enrollmentService.saveEnrollment(enrollment); }

    @PutMapping("/updateEnrollment")
    public Enrollment updateEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.saveEnrollment(enrollment);
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id); }


    // ========================
    // Endpoints CQRS (nouveaux)
    // ========================

    // Command (Write)
    @PostMapping("/command/create")
    public void createEnrollmentCommand(@RequestBody CreateEnrollmentCommand command) {
        commandGateway.sendAndWait(command);
    }

    // Query (Read)
    @GetMapping("/query/{id}")
    public EnrollmentResponse getEnrollmentQuery(@PathVariable Long id) {
        return queryGateway.query(new GetEnrollmentByIdQuery(id), EnrollmentResponse.class)
                .join();
    }



}
