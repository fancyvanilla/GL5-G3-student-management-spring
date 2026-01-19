package org.microservices.student;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentController {
    @Autowired
    IStudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() { return studentService.getAllStudents(); }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) { return studentService.getStudentById(id); }

    @PostMapping
    public Student createStudent(@RequestBody Student student) { return studentService.saveStudent(student); }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) { studentService.deleteStudent(id); }

    @GetMapping("/{id}/summary")
    public StudentSummaryDto getStudentSummary(@PathVariable Long id) {

       return studentService.getStudentSummary(id);
    }

}
