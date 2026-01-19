package org.microservices.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;
    public List<Student> getAllStudents() { return studentRepository.findAll(); }
    public Student getStudentById(Long id) { return studentRepository.findById(id).orElse(null); }
    public Student saveStudent(Student student) { return studentRepository.save(student); }
    public void deleteStudent(Long id) { studentRepository.deleteById(id); }
    public StudentSummaryDto getStudentSummary(Long id) {
        Student student = studentRepository.findById(id).get();
            return new StudentSummaryDto(
                    student.getIdStudent(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail()
            );
    }
}
