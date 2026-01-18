package tn.esprit.departmentservice.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import tn.esprit.departmentservice.entities.Department;
import tn.esprit.departmentservice.services.IDepartmentService;

import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);


    @GetMapping("/getAllDepartment")
    public List<Department> getAllDepartment() {
        logger.info("Request to getAllDepartment on port: " + environment.getProperty("local.server.port"));
        return departmentService.getAllDepartments();
    }

    @GetMapping("/getDepartment/{id}")
    public Department getDepartment(@PathVariable Long id) {
        logger.info("Request to getDepartment with id {} on port: {}", id, environment.getProperty("local.server.port"));
        try {
            Department dep = departmentService.getDepartmentById(id);
            System.out.println(dep);
            return dep;
        }
        catch (Exception e){
            System.out.println("wait simulation over");

        }
        return null;
    }
@PostMapping("/createDepartment")
public Department createDepartment(@RequestBody Department department) {
    logger.info("Request to createDepartment on port: " + environment.getProperty("local.server.port"));
    return departmentService.saveDepartment(department); }

@PutMapping("/updateDepartment")
public Department updateDepartment(@RequestBody Department department) {
    logger.info("Request to updateDepartment on port: " + environment.getProperty("local.server.port"));
    return departmentService.saveDepartment(department);
}

@DeleteMapping("/deleteDepartment/{id}")
public void deleteDepartment(@PathVariable Long id) {
    logger.info("Request to deleteDepartment with id {} on port: {}", id, environment.getProperty("local.server.port"));
    departmentService.deleteDepartment(id); }
}
