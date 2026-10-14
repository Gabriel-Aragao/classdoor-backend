package com.classdoor.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "professors")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String department;

    private String email;

    @Column(length = 1000)
    private String bio;

    private String avatarUrl;

    private Double averageRating = 0.0;

    private Integer totalReviews = 0;

    private Double recommendationPercentage = 0.0;

    public Professor() {}

    public Professor(String name, String department, String email, String bio, String avatarUrl, Double averageRating, Integer totalReviews, Double recommendationPercentage) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.recommendationPercentage = recommendationPercentage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }

    public Double getRecommendationPercentage() { return recommendationPercentage; }
    public void setRecommendationPercentage(Double recommendationPercentage) { this.recommendationPercentage = recommendationPercentage; }
}
