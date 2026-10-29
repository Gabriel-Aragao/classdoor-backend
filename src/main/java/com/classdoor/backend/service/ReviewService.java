package com.classdoor.backend.service;

import com.classdoor.backend.dto.ReviewRequest;
import com.classdoor.backend.model.ClassRoom;
import com.classdoor.backend.model.Professor;
import com.classdoor.backend.model.Review;
import com.classdoor.backend.model.User;
import com.classdoor.backend.repository.ClassRoomRepository;
import com.classdoor.backend.repository.ProfessorRepository;
import com.classdoor.backend.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ProfessorRepository professorRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private static final String PEPPER = "classdoor-secure-hmac-pepper-2026";

    public ReviewService(ReviewRepository reviewRepository, ClassRoomRepository classRoomRepository, ProfessorRepository professorRepository) {
        this.reviewRepository = reviewRepository;
        this.classRoomRepository = classRoomRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional
    public Review submitReview(ReviewRequest request, User currentUser) {
        logger.info("attempting to submit review for user {} in class {}", currentUser.getId(), request.getClassId());
        ClassRoom classRoom = classRoomRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Turma (ClassRoom) não encontrada"));
        logger.info("Found class {}", classRoom.getId());
        // Rule: Toggle evaluation open check
        if (!classRoom.isEvaluationOpen()) {
            logger.info("Evaluation is closed for class {}", classRoom.getId());
            throw new IllegalStateException("As avaliações para esta turma estão fechadas pelo professor.");
        }
        logger.info("Evaluation is open for class {}", classRoom.getId());
        // Rule: Quorum minimum >= 5 students
        if (classRoom.getEnrolledStudentsCount() < 5) {
            logger.info("Class {} does not have enough students for a review", classRoom.getId());
            throw new IllegalStateException("A turma não atingiu o quórum mínimo de 5 alunos matriculados.");
        }
        logger.info("Class {} has enough students for a review", classRoom.getId());
        // Rule: Privacy & Uniqueness via HMAC-SHA256 audit_hash
        String auditHash = computeHmacSha256(currentUser.getEmail(), String.valueOf(request.getClassId()));
        logger.info("Audit hash for user {} and class {}: {}", currentUser.getId(), request.getClassId(), auditHash);
        if (reviewRepository.existsByAuditHash(auditHash)) {
            logger.info("User {} has already submitted a review for class {}", currentUser.getId(), request.getClassId());
            throw new IllegalStateException("Você já submeteu uma avaliação para esta turma.");
        }
        logger.info("User {} has not submitted a review for class {}", currentUser.getId(), request.getClassId());
        String displayName = request.isAnonymous() ? "Estudante Anônimo" : (request.getAuthorDisplayName() != null ? request.getAuthorDisplayName() : currentUser.getName());
        logger.info("Display name for review: {}", displayName);
        Review review = new Review(
                request.getProfessorId(),
                request.getCourseId(),
                request.getClassId(),
                request.getOverallRating(),
                request.getDifficultyRating(),
                request.getRecommend(),
                request.getComment(),
                request.isAnonymous(),
                displayName,
                auditHash
        );

        Review savedReview = reviewRepository.save(review);

        // Update Professor stats
        updateProfessorStats(request.getProfessorId());
        logger.info("Review saved successfully for user {} in class {}", currentUser.getId(), request.getClassId());
        return savedReview;
    }

    @Transactional
    public Review upvoteReview(Long reviewId) {
        logger.info("Attempting to upvote review {}", reviewId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada"));
        review.setUsefulCount(review.getUsefulCount() + 1);
        logger.info("Review {} upvoted successfully", reviewId);
        return reviewRepository.save(review);
    }

    public Page<Review> getReviewsByProfessor(Long professorId, Pageable pageable) {
        logger.info("Attempting to get reviews for professor {}", professorId);
        return reviewRepository.findByProfessorId(professorId, pageable);
    }

    public Page<Review> getReviewsByCourse(Long courseId, Pageable pageable) {
        logger.info("Attempting to get reviews for course {}", courseId);
        return reviewRepository.findByCourseId(courseId, pageable);
    }

    private void updateProfessorStats(Long professorId) {
        logger.info("Attempting to update professor stats for professor {}", professorId);
        List<Review> reviews = reviewRepository.findByProfessorId(professorId);
        if (reviews.isEmpty()) {
            logger.info("No reviews found for professor {}", professorId);
            return;
        }
        logger.info("Found {} reviews for professor {}", reviews.size(), professorId);

        double avgRating = reviews.stream().mapToDouble(Review::getOverallRating).average().orElse(0.0);
        long recommendCount = reviews.stream().filter(Review::getRecommend).count();
        double recommendPct = ((double) recommendCount / reviews.size()) * 100.0;
        logger.info("Calculated stats for professor {}: avgRating={}, recommendCount={}, recommendPct={}", professorId, avgRating, recommendCount, recommendPct);
        Professor professor = professorRepository.findById(professorId).orElseThrow();
        professor.setAverageRating(Math.round(avgRating * 100.0) / 100.0);
        professor.setTotalReviews(reviews.size());
        professor.setRecommendationPercentage(Math.round(recommendPct * 10.0) / 10.0);
        professorRepository.save(professor);
        logger.info("Professor stats updated successfully for professor {}", professorId);
    }

    private String computeHmacSha256(String data, String key) {
        logger.info("Attempting to compute HMAC-SHA256 for data: {} and key: {}", data, key);
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(PEPPER.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal((data + key).getBytes(StandardCharsets.UTF_8));
            logger.info("Hmac-SHA256 computed successfully for data: {} and key: {}", data, key);
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            logger.error("Error calculating hash", e);
            throw new RuntimeException("Error calculating hash", e);
        }
    }
}
