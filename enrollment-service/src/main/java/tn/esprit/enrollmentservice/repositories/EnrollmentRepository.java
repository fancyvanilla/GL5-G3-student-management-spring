package tn.esprit.enrollmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.enrollmentservice.entities.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {}
