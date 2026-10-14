package com.classdoor.backend.dto;

import jakarta.validation.constraints.*;

public class ReviewRequest {

    @NotNull(message = "Professor ID is required")
    private Long professorId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Class ID is required")
    private Long classId;

    @Min(1) @Max(5)
    @NotNull(message = "Overall rating is required")
    private Double overallRating;

    @Min(1) @Max(5)
    @NotNull(message = "Difficulty rating is required")
    private Double difficultyRating;

    @NotNull(message = "Recommendation is required")
    private Boolean recommend;

    @NotBlank(message = "Comment is required")
    @Size(min = 20, max = 1000, message = "Comment must be between 20 and 1000 characters")
    private String comment;

    private boolean isAnonymous = true;

    private String authorDisplayName;

    public ReviewRequest() {}

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
}
