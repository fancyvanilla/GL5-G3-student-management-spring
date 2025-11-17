package tn.esprit.studentmanagement;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;
import tn.esprit.studentmanagement.services.EnrollmentService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    public void testAddEnrollment() {
        Enrollment e = new Enrollment();
        e.setEnrollmentDate(LocalDate.now());
        e.setGrade(18.5);
        e.setStatus(Status.ACTIVE);

        Enrollment saved = enrollmentService.saveEnrollment(e);

        log.info("Saved Enrollment: {}", saved);
        assertNotNull(saved.getIdEnrollment());
        assertEquals(Status.ACTIVE, saved.getStatus());
    }

    @Test
    public void testGetEnrollmentById() {
        Enrollment e = new Enrollment();
        e.setEnrollmentDate(LocalDate.now());
        e.setGrade(15.0);
        e.setStatus(Status.COMPLETED);
        Enrollment saved = enrollmentService.saveEnrollment(e);

        Enrollment found = enrollmentService.getEnrollmentById(saved.getIdEnrollment());
        assertEquals(saved.getIdEnrollment(), found.getIdEnrollment());
        assertEquals(Status.COMPLETED, found.getStatus());
    }

    @Test
    public void testUpdateEnrollment() {
        Enrollment e = new Enrollment();
        e.setEnrollmentDate(LocalDate.now());
        e.setGrade(12.0);
        e.setStatus(Status.DROPPED);
        Enrollment saved = enrollmentService.saveEnrollment(e);

        saved.setGrade(19.0);
        saved.setStatus(Status.COMPLETED);
        Enrollment updated = enrollmentService.saveEnrollment(saved);

        assertEquals(19.0, updated.getGrade());
        assertEquals(Status.COMPLETED, updated.getStatus());
    }

    @Test
    public void testDeleteEnrollment() {
        Enrollment e = new Enrollment();
        e.setEnrollmentDate(LocalDate.now());
        e.setGrade(10.0);
        e.setStatus(Status.FAILED);
        Enrollment saved = enrollmentService.saveEnrollment(e);

        enrollmentService.deleteEnrollment(saved.getIdEnrollment());
        assertFalse(enrollmentRepository.findById(saved.getIdEnrollment()).isPresent());
    }
}
