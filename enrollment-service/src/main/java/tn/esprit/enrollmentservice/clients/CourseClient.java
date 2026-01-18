package tn.esprit.enrollmentservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/courses/getCourse/{id}")
    Object getCourseById(@PathVariable("id") Long id);
}
