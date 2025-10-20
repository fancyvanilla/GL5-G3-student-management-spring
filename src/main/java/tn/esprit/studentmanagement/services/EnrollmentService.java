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
        log.info("getAllEnrollments is being called by Haithem ");
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long idEnrollment) {
        log.info("getEnrollmentById is being called by Haithem ");
        return enrollmentRepository.findById(idEnrollment).get();
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        log.info("saveEnrollment is being called by Haithem ");
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deleteEnrollment(Long idEnrollment) {
        log.info("deleteEnrollment is being called by Haithem ");
        enrollmentRepository.deleteById(idEnrollment);
    }
}
