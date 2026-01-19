package org.microservices.department;

import java.util.List;

public interface IDepartmentService {
     List<Department> getAllDepartments();
     Department getDepartmentById(Long idDepartment);
     Department saveDepartment(Department department);
     void deleteDepartment(Long idDepartment);
}
