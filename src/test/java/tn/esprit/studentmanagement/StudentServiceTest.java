package tn.esprit.studentmanagement;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import tn.esprit.studentmanagement.services.StudentService;
import tn.esprit.studentmanagement.entities.Student;

import java.time.LocalDate;
import java.util.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
void testGetAllStudents() {
    List<Student> students = Arrays.asList(
       new Student(1L, "John", "Doe", "john@example.com", "12345678",
                         LocalDate.now(), "Tunis", null, new ArrayList<>()),
       new Student(2L, "Jane", "Smith", "jane@example.com", "87654321",
                         LocalDate.now(), "Sousse", null, new ArrayList<>())
    );

    when(studentRepository.findAll()).thenReturn(students);

    List<Student> result = studentService.getAllStudents();
    assertEquals(2, result.size());
    assertEquals("John", result.get(0).getFirstName());
    assertEquals("Jane", result.get(1).getFirstName());

    verify(studentRepository, times(1)).findAll();
}

@Test
void testGetStudentByIdNotFound() {
    when(studentRepository.findById(1L)).thenReturn(Optional.empty());

    Student result = studentService.getStudentById(1L);
    assertNull(result);

    verify(studentRepository, times(1)).findById(1L);
}

}
