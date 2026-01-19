package org.microservices.enrollment;


import java.util.List;

public interface IEnrollment {
     List<Enrollment> getAllEnrollments();
     EnrollmentResponse  getEnrollmentById(Long idEnrollment);
     Enrollment  saveEnrollment(Enrollment enrollment);
     void deleteEnrollment(Long idEnrollment);

}
