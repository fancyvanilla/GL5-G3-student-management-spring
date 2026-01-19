package org.microservices.enrollment;

public class StudentSummaryDto {

    private Long idStudent;
    private String firstName;
    private String lastName;
    private String email;

    public StudentSummaryDto() {
        this.idStudent = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
    }

    public StudentSummaryDto(Long idStudent, String firstName, String lastName, String email) {
        this.idStudent = idStudent;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}