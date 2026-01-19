package org.microservices.student;

public class StudentSummaryDto {

    private Long idStudent;
    private String firstName;
    private String lastName;
    private String email;

    public StudentSummaryDto(Long idStudent, String firstName, String lastName, String email) {
        this.idStudent = idStudent;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}