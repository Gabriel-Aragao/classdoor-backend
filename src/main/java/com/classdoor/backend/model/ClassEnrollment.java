package com.classdoor.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "class_enrollments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"classId", "studentEmail"})
})
public class ClassEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long classId;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String studentEmail;

    public ClassEnrollment() {}

    public ClassEnrollment(Long classId, String studentEmail) {
        this.classId = classId;
        this.studentEmail = studentEmail;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
}
