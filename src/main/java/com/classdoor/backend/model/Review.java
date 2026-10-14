package com.classdoor.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"auditHash"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long professorId;

    @NotNull
    private Long courseId;

    @NotNull
    private Long classId;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Double overallRating;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Double difficultyRating;

    @NotNull
    private Boolean recommend;

    @NotBlank
    @Column(length = 1000, nullable = false)
    private String comment;

    private boolean isAnonymous = true;

    private String authorDisplayName = "Estudante Anônimo";

    private int usefulCount = 0;

    @Column(nullable = false, unique = true)
    private String auditHash; // HMAC-SHA256 uniqueness hash

    private LocalDateTime createdAt = LocalDateTime.now();

    public Review() {}

    public Review(Long professorId, Long courseId, Long classId, Double overallRating, Double difficultyRating, Boolean recommend, String comment, boolean isAnonymous, String authorDisplayName, String auditHash) {
        this.professorId = professorId;
        this.courseId = courseId;
        this.classId = classId;
        this.overallRating = overallRating;
        this.difficultyRating = difficultyRating;
        this.recommend = recommend;
        this.comment = comment;
        this.isAnonymous = isAnonymous;
        this.authorDisplayName = authorDisplayName;
        this.auditHash = auditHash;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProfessorId() { return professorId; }
    public void setProfessorId(Long professorId) { this.professorId = professorId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public Double getOverallRating() { return overallRating; }
    public void setOverallRating(Double overallRating) { this.overallRating = overallRating; }

    public Double getDifficultyRating() { return difficultyRating; }
    public void setDifficultyRating(Double difficultyRating) { this.difficultyRating = difficultyRating; }

    public Boolean getRecommend() { return recommend; }
    public void setRecommend(Boolean recommend) { this.recommend = recommend; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isAnonymous() { return isAnonymous; }
    public void setAnonymous(boolean anonymous) { isAnonymous = anonymous; }

    public String getAuthorDisplayName() { return authorDisplayName; }
    public void setAuthorDisplayName(String authorDisplayName) { this.authorDisplayName = authorDisplayName; }

    public int getUsefulCount() { return usefulCount; }
    public void setUsefulCount(int usefulCount) { this.usefulCount = usefulCount; }

    public String getAuditHash() { return auditHash; }
    public void setAuditHash(String auditHash) { this.auditHash = auditHash; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
