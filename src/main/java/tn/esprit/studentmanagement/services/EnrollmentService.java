package tn.esprit.studentmanagement.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;
import tn.esprit.studentmanagement.entities.Enrollment;

import java.util.List;

@Service
@Slf4j
public class EnrollmentService implements IEnrollment {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> getAllEnrollments() {
        log.info("Fetching all enrollments... by Achraf");
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        log.info("Fetched {} enrollments successfully. by Achraf", enrollments.size());
        return enrollments;
    }

    @Override
    public Enrollment getEnrollmentById(Long idEnrollment) {
        log.info("Fetching enrollment with ID: {} ... by Achraf", idEnrollment);
        Enrollment enrollment = enrollmentRepository.findById(idEnrollment)
                .orElse(null);
        if (enrollment != null) {
            log.info("Enrollment with ID {} found. by Achraf", idEnrollment);
        } else {
            log.warn("Enrollment with ID {} not found! by Achraf", idEnrollment);
        }
        return enrollment;
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        log.info("Saving enrollment for student ID: {} ... by Achraf",
                enrollment.getStudent() != null ? enrollment.getStudent().getId() : "unknown");
        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("Enrollment saved successfully with ID: {}. by Achraf", saved.getIdEnrollment());
        return saved;
    }

    @Override
    public void deleteEnrollment(Long idEnrollment) {
        log.info("Deleting enrollment with ID: {} ... by Achraf", idEnrollment);
        enrollmentRepository.deleteById(idEnrollment);
        log.info("Enrollment with ID {} deleted successfully. by Achraf", idEnrollment);
    }
}
