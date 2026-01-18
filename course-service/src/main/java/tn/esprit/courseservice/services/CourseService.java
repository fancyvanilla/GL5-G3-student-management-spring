package tn.esprit.courseservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.courseservice.entities.Course;
import tn.esprit.courseservice.repositories.CourseRepository;

import java.util.List;

@Service
public class CourseService implements ICourseService {
    @Autowired
    CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long idCourse) {
        return courseRepository.findById(idCourse).orElse(null);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long idCourse) {
        courseRepository.deleteById(idCourse);
    }
}
