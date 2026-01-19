package org.microservices.enrollment;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.microservices.enrollment.client.StudentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class EnrollmentService implements IEnrollment {
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired(required = false)
    StudentClient studentClient;

    @Override
    public List<Enrollment> getAllEnrollments() {
        log.info("getAllEnrollments is being called");
        return enrollmentRepository.findAll();
    }

    @Override
    @CircuitBreaker(name = "studentServiceCircuitBreaker", fallbackMethod = "getEnrollmentByIdFallback")
    public EnrollmentResponse getEnrollmentById(Long idEnrollment) {
        log.info("Fetching enrollment with id={}", idEnrollment);

        Enrollment enrollment = enrollmentRepository.findById(idEnrollment)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enrollment not found"));

        // Simulate failure 50% of the time
        if (Math.random() > 0.5) {
            log.error("Simulated failure for enrollment id={}", idEnrollment);
            throw new RuntimeException("Simulated failure");
        }

        StudentSummaryDto student = null;
        if (studentClient != null) {
            try {
                student = studentClient.getStudentSummary(enrollment.getStudent());
            } catch (Exception e) {
                log.warn("Failed to fetch student summary", e);
            }
        }

        return mapToResponse(enrollment, student);
    }

    // Fallback method - must have same parameters + Throwable
    public String getEnrollmentByIdFallback(Throwable t) {
        return "Weather data is currently unavailable";
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        log.info("saveEnrollment is being called");
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long idEnrollment) {
        log.info("deleteEnrollment is being called");
        enrollmentRepository.deleteById(idEnrollment);
    }

    private EnrollmentResponse mapToResponse(
            Enrollment enrollment,
            StudentSummaryDto student) {

        EnrollmentResponse response = new EnrollmentResponse();
        response.setIdEnrollment(enrollment.getIdEnrollment());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        response.setGrade(enrollment.getGrade());
        response.setStatus(enrollment.getStatus());
        response.setStudent(student);
        response.setCourse(enrollment.getCourse());
        return response;
    }
}
