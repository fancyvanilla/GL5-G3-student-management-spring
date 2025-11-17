package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;
import tn.esprit.studentmanagement.services.EnrollmentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
@SpringBootTest
public class EnrollmentServiceMockitoTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    public EnrollmentServiceMockitoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEnrollment() {
        Enrollment e = new Enrollment(null, LocalDate.now(), 17.5, Status.ACTIVE, null, null);
        when(enrollmentRepository.save(e)).thenReturn(new Enrollment(1L, e.getEnrollmentDate(), e.getGrade(), e.getStatus(), null, null));

        Enrollment result = enrollmentService.saveEnrollment(e);

        assertNotNull(result.getIdEnrollment());
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    @Test
    public void testGetAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(
                new Enrollment(1L, LocalDate.now(), 15.0, Status.COMPLETED, null, null)
        ));

        assertEquals(1, enrollmentService.getAllEnrollments().size());
    }

    @Test
    public void testGetEnrollmentById() {
        Enrollment e = new Enrollment(1L, LocalDate.now(), 14.0, Status.DROPPED, null, null);
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e));

        Enrollment found = enrollmentService.getEnrollmentById(1L);
        assertEquals(14.0, found.getGrade());
        assertEquals(Status.DROPPED, found.getStatus());
    }

    @Test
    public void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);
        enrollmentService.deleteEnrollment(1L);
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
