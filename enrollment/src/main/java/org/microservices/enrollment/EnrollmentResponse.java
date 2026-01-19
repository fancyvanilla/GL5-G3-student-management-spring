package org.microservices.enrollment;

import java.time.LocalDate;

public class EnrollmentResponse {
    private Long idEnrollment;
    private LocalDate enrollmentDate;
    private Double grade;
    private Status status;
    private StudentSummaryDto student;
    private Course course;

    public Long getIdEnrollment() {
        return idEnrollment;
    }

    public void setIdEnrollment(Long idEnrollment) {
        this.idEnrollment = idEnrollment;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StudentSummaryDto getStudent() {
        return student;
    }

    public void setStudent(StudentSummaryDto student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
