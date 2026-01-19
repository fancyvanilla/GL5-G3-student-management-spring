package org.microservices.enrollment.command;

import java.time.LocalDate;

public record CreateEnrollmentCommand(
        Long idEnrollment,
        Long studentId,
        Long courseId,
        LocalDate enrollmentDate
) {}