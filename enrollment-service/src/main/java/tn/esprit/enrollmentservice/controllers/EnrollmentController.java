package tn.esprit.enrollmentservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.enrollmentservice.entities.Enrollment;
import tn.esprit.enrollmentservice.services.IEnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class EnrollmentController {
    @Autowired
    private IEnrollmentService enrollmentService;

    @GetMapping("/getAllEnrollments")
    public List<Enrollment> getAllEnrollments() { return enrollmentService.getAllEnrollments(); }

    @GetMapping("/getEnrollment/{id}")
    public Enrollment getEnrollment(@PathVariable Long id) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        System.out.println(enrollment);
        return enrollment;
    }

    @PostMapping("/createEnrollment")
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) { return enrollmentService.saveEnrollment(enrollment); }

    @PutMapping("/updateEnrollment")
    public Enrollment updateEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.saveEnrollment(enrollment);
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}
