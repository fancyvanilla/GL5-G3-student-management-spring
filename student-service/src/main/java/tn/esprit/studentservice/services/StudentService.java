package tn.esprit.studentservice.services;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentservice.clients.DepartmentClient;
import tn.esprit.studentservice.entities.DepartmentResponse;
import tn.esprit.studentservice.entities.Student;
import tn.esprit.studentservice.repositories.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentClient departmentClient;
    public List<Student> getAllStudents() { return studentRepository.findAll(); }
    public Student getStudentById(Long id) { return studentRepository.findById(id).orElse(null); }
    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackSaveStudent")
    @Retry(name = "departmentService")
    @RateLimiter(name = "departmentService")
    public DepartmentResponse saveStudent(Student student) {
        System.out.println("retry!");
        if (departmentClient.getDepartmentById(student.getDepartmentId()) != null) {
            return new DepartmentResponse(Optional.of(studentRepository.save(student)),true)  ;
        }
        else {
            return new DepartmentResponse(null,true)  ;
        }
    }
    public DepartmentResponse fallbackSaveStudent(Student student, Throwable t) {
        System.out.println("fallback working!");
        return new DepartmentResponse(null,false)  ;
    }
    public void deleteStudent(Long id) { studentRepository.deleteById(id); }

}
