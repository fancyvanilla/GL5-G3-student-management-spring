package org.microservices.student;


import java.util.List;

public interface IStudentService {
    public List<Student> getAllStudents();
    public Student getStudentById(Long id);
    public Student saveStudent(Student student);
    public void deleteStudent(Long id);
    public StudentSummaryDto getStudentSummary(Long id);
}
