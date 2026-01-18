package tn.esprit.courseservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.courseservice.entities.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {}
