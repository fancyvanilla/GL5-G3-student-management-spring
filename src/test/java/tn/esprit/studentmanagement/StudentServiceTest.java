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
        new Student(1L, "Alice"),
        new Student(2L, "Bob")
    );

    when(studentRepository.findAll()).thenReturn(students);

    List<Student> result = studentService.getAllStudents();
    assertEquals(2, result.size());
    assertEquals("Alice", result.get(0).getFirstName());

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
