package tn.esprit.courseservice.services;

import tn.esprit.courseservice.entities.Course;

import java.util.List;

public interface ICourseService {
    public List<Course> getAllCourses();
    public Course getCourseById(Long idCourse);
    public Course saveCourse(Course course);
    public void deleteCourse(Long idCourse);
}
