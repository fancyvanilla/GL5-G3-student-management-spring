package tn.esprit.enrollmentservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnrollment;
    private Long studentId;
    private Long courseId;
    private String semester;
}
