package tn.esprit.courseservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.courseservice.entities.Course;
import tn.esprit.courseservice.services.ICourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class CourseController {
    @Autowired
    private ICourseService courseService;

    @GetMapping("/getAllCourses")
    public List<Course> getAllCourses() { return courseService.getAllCourses(); }

    @GetMapping("/getCourse/{id}")
    public Course getCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        System.out.println(course);
        return course;
    }

    @PostMapping("/createCourse")
    public Course createCourse(@RequestBody Course course) { return courseService.saveCourse(course); }

    @PutMapping("/updateCourse")
    public Course updateCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }

    @DeleteMapping("/deleteCourse/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
