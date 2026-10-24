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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<Review> submitReview(
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        logger.info("Attempting submission of review {} received by {} ", request, currentUser.getEmail());
        Review review = reviewService.submitReview(request, currentUser);
        logger.info("Successfully submitted review {} with id: {}", review, review.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PostMapping({"/{id}/useful", "/{id}/upvote"})
    public ResponseEntity<Review> upvoteReview(@PathVariable Long id) {
        logger.info("Attempting upvote for review {}", id);
        Review review = reviewService.upvoteReview(id);
        logger.info("Successfully upvoted review {}", id);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<Page<Review>> getReviews(
            @RequestParam(required = false) Long professorId,
            @RequestParam(required = false) Long courseId,
            Pageable pageable
    ) {
        logger.info("attempting to retreive reviews with professorId: {} and courseId: {}", professorId, courseId);
        Page<Review> reviews;
        if (professorId != null) {
            reviews = reviewService.getReviewsByProfessor(professorId, pageable);
        } else if (courseId != null) {
            reviews = reviewService.getReviewsByCourse(courseId, pageable);
        } else {
            reviews = Page.empty(pageable);

        }
        logger.info("Successfully retreived reviews: {}", reviews);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/professors/{professorId}/reviews")
    public ResponseEntity<Page<Review>> getReviewsByProfessorPath(
            @PathVariable Long professorId,
            Pageable pageable
    ) {
        logger.info("attempting to retreive reviews for professor {}", professorId);
        Page<Review> reviews = reviewService.getReviewsByProfessor(professorId, pageable);
        logger.info("Successfully retreived reviews: {}", reviews);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/courses/{courseId}/reviews")
    public ResponseEntity<Page<Review>> getReviewsByCoursePath(
            @PathVariable Long courseId,
            Pageable pageable
    ) {
        logger.info("attempting to retreive reviews for course {}", courseId);
        Page<Review> reviews = reviewService.getReviewsByCourse(courseId, pageable);
        logger.info("Successfully retreived reviews: {}", reviews);
        return ResponseEntity.ok(reviews);
    }
}
