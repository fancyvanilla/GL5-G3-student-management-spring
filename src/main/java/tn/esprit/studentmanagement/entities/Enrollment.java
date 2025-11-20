package tn.esprit.studentmanagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate enrollmentDate;
    private Double grade;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JsonBackReference("student-enrollments")
    private Student student;

    @ManyToOne
    @JsonBackReference("course-enrollments")
    private Course course;





}
