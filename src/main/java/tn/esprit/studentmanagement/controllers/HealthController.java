package tn.esprit.studentmanagement.controllers;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class HealthController {
    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }
}
