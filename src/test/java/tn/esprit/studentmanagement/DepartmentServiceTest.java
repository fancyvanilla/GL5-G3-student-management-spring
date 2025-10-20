package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.DepartmentService;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void testSaveDepartment() {
        Department dept = new Department();
        dept.setName("IT");

        when(departmentRepository.save(dept)).thenReturn(dept);

        Department result = departmentService.saveDepartment(dept);

        assertEquals("IT", result.getName());
        verify(departmentRepository, times(1)).save(dept);
    }

    @Test
    public void testGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(new Department(), new Department()));

        List<Department> departments = departmentService.getAllDepartments();

        assertEquals(2, departments.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    public void testGetDepartmentById() {
        Department dept = new Department();
        dept.setIdDepartment(1L);
        dept.setName("Marketing");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));

        Department result = departmentService.getDepartmentById(1L);

        assertEquals("Marketing", result.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteDepartment() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
