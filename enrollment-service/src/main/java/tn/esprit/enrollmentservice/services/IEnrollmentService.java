package tn.esprit.enrollmentservice.services;

import tn.esprit.enrollmentservice.entities.Enrollment;

import java.util.List;

public interface IEnrollmentService {
    public List<Enrollment> getAllEnrollments();
    public Enrollment getEnrollmentById(Long idEnrollment);
    public Enrollment saveEnrollment(Enrollment enrollment);
    public void deleteEnrollment(Long idEnrollment);
}
