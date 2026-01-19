package org.microservices.enrollment;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@AllArgsConstructor
public class EnrollmentController {
    IEnrollment enrollmentService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Enrollment service is healthy");
    }

    @GetMapping
    public List<Enrollment> getAllEnrollment() { 
        return enrollmentService.getAllEnrollments(); 
    }

    @GetMapping("/{id}")
    public EnrollmentResponse getEnrollment(@PathVariable Long id) { 
        return enrollmentService.getEnrollmentById(id); 
    }

    @PostMapping
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) { 
        return enrollmentService.saveEnrollment(enrollment); 
    }

    @PutMapping
    public Enrollment updateEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.saveEnrollment(enrollment);
    }

    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id); 
    }

}
