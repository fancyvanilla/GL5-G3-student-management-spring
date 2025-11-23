package tn.esprit.studentservice.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentservice.clients.DepartmentClient;
import tn.esprit.studentservice.entities.Student;
import tn.esprit.studentservice.repositories.StudentRepository;

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
    public Optional<Student> saveStudent(Student student) {
        if (departmentClient.getDepartmentById(student.getDepartmentId()) != null) {
            return Optional.of(studentRepository.save(student));
        }
        else {
            return Optional.empty();
        }
    }
    public void deleteStudent(Long id) { studentRepository.deleteById(id); }

}
