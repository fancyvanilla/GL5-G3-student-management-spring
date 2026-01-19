package org.microservices.enrollment.command;

import lombok.RequiredArgsConstructor;
// import org.axonframework.commandhandling.CommandHandler;
import org.microservices.enrollment.Enrollment;
import org.microservices.enrollment.EnrollmentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentCommandHandler {

    private final EnrollmentRepository enrollmentRepository;

    // @CommandHandler - DISABLED: Axon Framework not available
    public void handle(CreateEnrollmentCommand command) {

        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(command.idEnrollment());
        enrollment.setStudent(command.studentId());
        enrollment.setCourse(null);
        enrollment.setEnrollmentDate(command.enrollmentDate());

        enrollmentRepository.save(enrollment);
    }
}