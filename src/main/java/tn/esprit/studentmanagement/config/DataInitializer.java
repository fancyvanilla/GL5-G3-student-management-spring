package tn.esprit.studentmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.studentmanagement.entities.*;
import tn.esprit.studentmanagement.repositories.*;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        departmentRepository.deleteAll();

        System.out.println("🗑️ Cleared existing data");

        // Create Departments
        Department computerScience = new Department();
        computerScience.setName("Computer Science");
        computerScience.setLocation("Building A, 3rd Floor");
        computerScience.setPhone("+216 71 123 456");
        computerScience.setHead("Dr. Karim Mansouri");

        Department mathematics = new Department();
        mathematics.setName("Mathematics");
        mathematics.setLocation("Building B, 2nd Floor");
        mathematics.setPhone("+216 71 234 567");
        mathematics.setHead("Dr. Amira Salem");

        Department engineering = new Department();
        engineering.setName("Engineering");
        engineering.setLocation("Building C, 1st Floor");
        engineering.setPhone("+216 71 345 678");
        engineering.setHead("Dr. Youssef Khaled");

        departmentRepository.saveAll(Arrays.asList(computerScience, mathematics, engineering));
        System.out.println("✅ Created 3 departments");

        // Create Students
        Student student1 = new Student();
        student1.setFirstName("Ahmed");
        student1.setLastName("Ben Ali");
        student1.setEmail("ahmed.benali@esprit.tn");
        student1.setPhone("+216 20 123 456");
        student1.setDateOfBirth(LocalDate.of(2002, 5, 15));
        student1.setAddress("Tunis, Tunisia");
        student1.setDepartment(computerScience);

        Student student2 = new Student();
        student2.setFirstName("Fatima");
        student2.setLastName("Trabelsi");
        student2.setEmail("fatima.trabelsi@esprit.tn");
        student2.setPhone("+216 22 234 567");
        student2.setDateOfBirth(LocalDate.of(2003, 8, 22));
        student2.setAddress("Ariana, Tunisia");
        student2.setDepartment(computerScience);

        Student student3 = new Student();
        student3.setFirstName("Mohamed");
        student3.setLastName("Gharbi");
        student3.setEmail("mohamed.gharbi@esprit.tn");
        student3.setPhone("+216 24 345 678");
        student3.setDateOfBirth(LocalDate.of(2001, 12, 10));
        student3.setAddress("Sousse, Tunisia");
        student3.setDepartment(engineering);

        Student student4 = new Student();
        student4.setFirstName("Salma");
        student4.setLastName("Khelifi");
        student4.setEmail("salma.khelifi@esprit.tn");
        student4.setPhone("+216 26 456 789");
        student4.setDateOfBirth(LocalDate.of(2002, 3, 18));
        student4.setAddress("Sfax, Tunisia");
        student4.setDepartment(mathematics);

        Student student5 = new Student();
        student5.setFirstName("Hichem");
        student5.setLastName("Dridi");
        student5.setEmail("hichem.dridi@esprit.tn");
        student5.setPhone("+216 28 567 890");
        student5.setDateOfBirth(LocalDate.of(2003, 7, 5));
        student5.setAddress("Bizerte, Tunisia");
        student5.setDepartment(engineering);

        studentRepository.saveAll(Arrays.asList(student1, student2, student3, student4, student5));
        System.out.println("✅ Created 5 students");

        // Create Courses
        Course course1 = new Course();
        course1.setName("Spring Boot Development");
        course1.setCode("CS301");
        course1.setCredit(3);
        course1.setDescription("Learn to build enterprise applications with Spring Boot framework");

        Course course2 = new Course();
        course2.setName("Database Management Systems");
        course2.setCode("CS201");
        course2.setCredit(4);
        course2.setDescription("Introduction to relational databases, SQL, and database design");

        Course course3 = new Course();
        course3.setName("Web Development");
        course3.setCode("CS302");
        course3.setCredit(3);
        course3.setDescription("Full-stack web development with modern frameworks");

        Course course4 = new Course();
        course4.setName("Data Structures and Algorithms");
        course4.setCode("CS101");
        course4.setCredit(4);
        course4.setDescription("Fundamental data structures and algorithm design");

        Course course5 = new Course();
        course5.setName("Software Engineering");
        course5.setCode("CS401");
        course5.setCredit(3);
        course5.setDescription("Software development methodologies and best practices");

        Course course6 = new Course();
        course6.setName("Microservices Architecture");
        course6.setCode("CS501");
        course6.setCredit(3);
        course6.setDescription("Design and implementation of microservices-based systems");

        courseRepository.saveAll(Arrays.asList(course1, course2, course3, course4, course5, course6));
        System.out.println("✅ Created 6 courses");

        // Create Enrollments with different statuses
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);
        enrollment1.setCourse(course1);
        enrollment1.setEnrollmentDate(LocalDate.now().minusDays(30));
        enrollment1.setGrade(85.5);
        enrollment1.setStatus(Status.ACTIVE);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student1);
        enrollment2.setCourse(course2);
        enrollment2.setEnrollmentDate(LocalDate.now().minusDays(60));
        enrollment2.setGrade(90.0);
        enrollment2.setStatus(Status.COMPLETED);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStudent(student2);
        enrollment3.setCourse(course1);
        enrollment3.setEnrollmentDate(LocalDate.now().minusDays(20));
        enrollment3.setGrade(78.0);
        enrollment3.setStatus(Status.ACTIVE);

        Enrollment enrollment4 = new Enrollment();
        enrollment4.setStudent(student2);
        enrollment4.setCourse(course3);
        enrollment4.setEnrollmentDate(LocalDate.now().minusDays(90));
        enrollment4.setGrade(55.0);
        enrollment4.setStatus(Status.FAILED);

        Enrollment enrollment5 = new Enrollment();
        enrollment5.setStudent(student3);
        enrollment5.setCourse(course2);
        enrollment5.setEnrollmentDate(LocalDate.now().minusDays(70));
        enrollment5.setGrade(92.5);
        enrollment5.setStatus(Status.COMPLETED);

        Enrollment enrollment6 = new Enrollment();
        enrollment6.setStudent(student3);
        enrollment6.setCourse(course4);
        enrollment6.setEnrollmentDate(LocalDate.now().minusDays(40));
        enrollment6.setGrade(88.0);
        enrollment6.setStatus(Status.COMPLETED);

        Enrollment enrollment7 = new Enrollment();
        enrollment7.setStudent(student4);
        enrollment7.setCourse(course4);
        enrollment7.setEnrollmentDate(LocalDate.now().minusDays(35));
        enrollment7.setGrade(95.0);
        enrollment7.setStatus(Status.COMPLETED);

        Enrollment enrollment8 = new Enrollment();
        enrollment8.setStudent(student4);
        enrollment8.setCourse(course5);
        enrollment8.setEnrollmentDate(LocalDate.now().minusDays(5));
        enrollment8.setGrade(null);
        enrollment8.setStatus(Status.ACTIVE);

        Enrollment enrollment9 = new Enrollment();
        enrollment9.setStudent(student5);
        enrollment9.setCourse(course6);
        enrollment9.setEnrollmentDate(LocalDate.now().minusDays(15));
        enrollment9.setGrade(null);
        enrollment9.setStatus(Status.DROPPED);

        Enrollment enrollment10 = new Enrollment();
        enrollment10.setStudent(student5);
        enrollment10.setCourse(course3);
        enrollment10.setEnrollmentDate(LocalDate.now().minusDays(50));
        enrollment10.setGrade(null);
        enrollment10.setStatus(Status.WITHDRAWN);

        enrollmentRepository.saveAll(Arrays.asList(
                enrollment1, enrollment2, enrollment3, enrollment4, enrollment5,
                enrollment6, enrollment7, enrollment8, enrollment9, enrollment10
        ));
        System.out.println("✅ Created 10 enrollments");

        System.out.println("🎉 Database initialized successfully!");
        System.out.println("📊 Summary:");
        System.out.println("   - 3 departments");
        System.out.println("   - 5 students");
        System.out.println("   - 6 courses");
        System.out.println("   - 10 enrollments (various statuses)");
    }
}