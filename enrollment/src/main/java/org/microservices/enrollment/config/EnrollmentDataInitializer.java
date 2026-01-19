package org.microservices.enrollment.config;

import lombok.RequiredArgsConstructor;
import org.microservices.enrollment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class EnrollmentDataInitializer implements CommandLineRunner {
    @Autowired
     EnrollmentRepository enrollmentRepository;
    @Autowired
     CourseRepository courseRepository;

    @Override
    public void run(String... args) {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();

        // Create courses
        Course c1 = new Course();
        c1.setName("Spring Boot");
        c1.setCode("CS301");
        c1.setCredit(3);

        Course c2 = new Course();
        c2.setName("Database Systems");
        c2.setCode("CS201");
        c2.setCredit(4);

        courseRepository.saveAll(List.of(c1, c2));

        // Create enrollments - students already exist with generated IDs
        // You'll need to find students by email or use a known pattern

        Enrollment e1 = new Enrollment();
        e1.setEnrollmentDate(LocalDate.now().minusDays(20));
        e1.setGrade(85.0);
        e1.setStatus(Status.ACTIVE);
        e1.setStudent(92L); // First generated student ID
        e1.setCourse(c1);

        Enrollment e2 = new Enrollment();
        e2.setEnrollmentDate(LocalDate.now().minusDays(60));
        e2.setGrade(90.0);
        e2.setStatus(Status.COMPLETED);
        e2.setStudent(1L); // First generated student ID
        e2.setCourse(c2);

        Enrollment e3 = new Enrollment();
        e3.setEnrollmentDate(LocalDate.now().minusDays(10));
        e3.setGrade(null);
        e3.setStatus(Status.ACTIVE);
        e3.setStudent(2L); // Second generated student ID
        e3.setCourse(c1);

        List<Enrollment> savedEnrollments = enrollmentRepository.saveAll(List.of(e1, e2, e3));

        savedEnrollments.forEach(e ->
                System.out.println("✅ Enrollment saved with ID: " + e.getIdEnrollment() +
                        " for student: " + e.getStudent())
        );

        System.out.println("✅ Enrollments & courses initialized");
    }
}