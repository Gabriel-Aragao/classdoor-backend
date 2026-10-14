package com.classdoor.backend.controller;

import com.classdoor.backend.dto.ReviewRequest;
import com.classdoor.backend.model.Review;
import com.classdoor.backend.model.User;
import com.classdoor.backend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<Review> submitReview(
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        Review review = reviewService.submitReview(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PostMapping("/{id}/useful")
    public ResponseEntity<Review> upvoteReview(@PathVariable Long id) {
        Review review = reviewService.upvoteReview(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<Page<Review>> getReviews(
            @RequestParam(required = false) Long professorId,
            @RequestParam(required = false) Long courseId,
            Pageable pageable
    ) {
        Page<Review> reviews;
        if (professorId != null) {
            reviews = reviewService.getReviewsByProfessor(professorId, pageable);
        } else if (courseId != null) {
            reviews = reviewService.getReviewsByCourse(courseId, pageable);
        } else {
            // Default empty or handle general
            reviews = Page.empty(pageable);
        }
        return ResponseEntity.ok(reviews);
    }
}
