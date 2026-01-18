package tn.esprit.studentservice.entities;

import jakarta.persistence.Entity;
import lombok.*;
import tn.esprit.studentservice.model.Department;

import java.util.Optional;
@AllArgsConstructor
public class DepartmentResponse {
    @Getter
    private Optional<Student> department;
    @Getter
    private boolean serviceAvailable;

}
