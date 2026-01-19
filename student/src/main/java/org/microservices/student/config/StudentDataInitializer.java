package org.microservices.student.config;

import lombok.RequiredArgsConstructor;
import org.microservices.student.Student;
import org.microservices.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1) // Run first
@RequiredArgsConstructor
public class StudentDataInitializer implements CommandLineRunner {
    @Autowired
     StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        studentRepository.deleteAll();

        Student s1 = new Student();
        s1.setFirstName("Ahmed");
        s1.setLastName("Ben Ali");
        s1.setEmail("ahmed.benali@esprit.tn");
        s1.setPhone("+216 20 123 456");
        s1.setDateOfBirth(LocalDate.of(2002, 5, 15));
        s1.setAddress("Tunis");
        s1.setDepartment(1L);
        s1.setEnrollments(new ArrayList<>()); // Initialize empty

        Student s2 = new Student();
        s2.setFirstName("Fatima");
        s2.setLastName("Trabelsi");
        s2.setEmail("fatima.trabelsi@esprit.tn");
        s2.setPhone("+216 22 234 567");
        s2.setDateOfBirth(LocalDate.of(2003, 8, 22));
        s2.setAddress("Ariana");
        s2.setDepartment(1L);
        s2.setEnrollments(new ArrayList<>());

        Student s3 = new Student();
        s3.setFirstName("Mohamed");
        s3.setLastName("Gharbi");
        s3.setEmail("mohamed.gharbi@esprit.tn");
        s3.setPhone("+216 24 345 678");
        s3.setDateOfBirth(LocalDate.of(2001, 12, 10));
        s3.setAddress("Sousse");
        s3.setDepartment(3L);
        s3.setEnrollments(new ArrayList<>());

        List<Student> savedStudents = studentRepository.saveAll(List.of(s1, s2, s3));

        savedStudents.forEach(s ->
                System.out.println("✅ Student saved: " + s.getFirstName() + " with ID: " + s.getIdStudent())
        );

        System.out.println("✅ Students initialized");
    }
}