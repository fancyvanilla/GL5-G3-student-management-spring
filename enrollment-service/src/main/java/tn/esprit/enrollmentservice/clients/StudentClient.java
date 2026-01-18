package tn.esprit.enrollmentservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service")
public interface StudentClient {
    @GetMapping("/students/getStudent/{id}")
    Object getStudentById(@PathVariable("id") Long id);
}
