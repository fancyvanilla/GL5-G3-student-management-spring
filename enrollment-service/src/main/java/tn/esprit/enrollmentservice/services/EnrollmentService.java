package tn.esprit.enrollmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.enrollmentservice.clients.CourseClient;
import tn.esprit.enrollmentservice.clients.StudentClient;
import tn.esprit.enrollmentservice.entities.Enrollment;
import tn.esprit.enrollmentservice.repositories.EnrollmentRepository;

import java.util.List;

@Service
public class EnrollmentService implements IEnrollmentService {
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    StudentClient studentClient;
    @Autowired
    CourseClient courseClient;

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long idEnrollment) {
        return enrollmentRepository.findById(idEnrollment).orElse(null);
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        // check if student and course exist
        if (studentClient.getStudentById(enrollment.getStudentId()) != null &&
            courseClient.getCourseById(enrollment.getCourseId()) != null) {
            return enrollmentRepository.save(enrollment);
        }
        return null;
    }

    @Override
    public void deleteEnrollment(Long idEnrollment) {
        enrollmentRepository.deleteById(idEnrollment);
    }
}
