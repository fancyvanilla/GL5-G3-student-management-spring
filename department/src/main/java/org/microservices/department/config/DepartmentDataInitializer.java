package org.microservices.department.config;

import lombok.RequiredArgsConstructor;
import org.microservices.department.Department;
import org.microservices.department.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentDataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) {

        departmentRepository.deleteAll();

        Department cs = new Department();
        cs.setName("Computer Science");
        cs.setLocation("Building A");
        cs.setPhone("+216 71 123 456");
        cs.setHead("Dr. Karim Mansouri");
        cs.setStudents(List.of(1L, 2L));

        Department math = new Department();
        math.setName("Mathematics");
        math.setLocation("Building B");
        math.setPhone("+216 71 234 567");
        math.setHead("Dr. Amira Salem");
        math.setStudents(List.of(3L));

        Department eng = new Department();
        eng.setName("Engineering");
        eng.setLocation("Building C");
        eng.setPhone("+216 71 345 678");
        eng.setHead("Dr. Youssef Khaled");
        eng.setStudents(List.of(4L, 5L));

        departmentRepository.saveAll(List.of(cs, math, eng));

        System.out.println("✅ Departments initialized");
    }
}
