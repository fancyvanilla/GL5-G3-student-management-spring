package org.microservices.enrollment.query;

import lombok.RequiredArgsConstructor;
// import org.axonframework.queryhandling.QueryHandler;
import org.microservices.enrollment.Enrollment;
import org.microservices.enrollment.EnrollmentRepository;
import org.microservices.enrollment.EnrollmentResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentQueryHandler {

    private final EnrollmentRepository enrollmentRepository;

    // @QueryHandler - DISABLED: Axon Framework not available
    public EnrollmentResponse handle(GetEnrollmentByIdQuery query) {

        Enrollment enrollment = enrollmentRepository.findById(query.idEnrollment())
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        EnrollmentResponse response = new EnrollmentResponse();
        response.setIdEnrollment(enrollment.getIdEnrollment());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        response.setGrade(enrollment.getGrade());
        response.setStatus(enrollment.getStatus());
        response.setStudent(null);
        response.setCourse(enrollment.getCourse());

        return response;
    }
}
