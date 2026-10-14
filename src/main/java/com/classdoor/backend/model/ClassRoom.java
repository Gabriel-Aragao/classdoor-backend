package com.classdoor.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "class_rooms")
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long courseId;

    @NotNull
    private Long professorId;

    @NotBlank
    @Column(nullable = false)
    private String semester; // ex: "2026.1"

    private boolean isEvaluationOpen = false;

    @Column(nullable = false)
    private String policy = "ANONYMOUS_ONLY"; // ANONYMOUS_ONLY or ALLOW_IDENTIFIED

    private int enrolledStudentsCount = 0; // Quorum minimum >= 5 required

    public ClassRoom() {}

    public ClassRoom(Long courseId, Long professorId, String semester, boolean isEvaluationOpen, String policy, int enrolledStudentsCount) {
        this.courseId = courseId;
        this.professorId = professorId;
        this.semester = semester;
        this.isEvaluationOpen = isEvaluationOpen;
        this.policy = policy;
        this.enrolledStudentsCount = enrolledStudentsCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getProfessorId() { return professorId; }
    public void setProfessorId(Long professorId) { this.professorId = professorId; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public boolean isEvaluationOpen() { return isEvaluationOpen; }
    public void setEvaluationOpen(boolean evaluationOpen) { isEvaluationOpen = evaluationOpen; }

    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }

    public int getEnrolledStudentsCount() { return enrolledStudentsCount; }
    public void setEnrolledStudentsCount(int enrolledStudentsCount) { this.enrolledStudentsCount = enrolledStudentsCount; }
}
