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

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ProfessorRepository professorRepository;

    private static final String PEPPER = "classdoor-secure-hmac-pepper-2026";

    public ReviewService(ReviewRepository reviewRepository, ClassRoomRepository classRoomRepository, ProfessorRepository professorRepository) {
        this.reviewRepository = reviewRepository;
        this.classRoomRepository = classRoomRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional
    public Review submitReview(ReviewRequest request, User currentUser) {
        ClassRoom classRoom = classRoomRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Turma (ClassRoom) não encontrada"));

        // Rule: Toggle evaluation open check
        if (!classRoom.isEvaluationOpen()) {
            throw new IllegalStateException("As avaliações para esta turma estão fechadas pelo professor.");
        }

        // Rule: Quorum minimum >= 5 students
        if (classRoom.getEnrolledStudentsCount() < 5) {
            throw new IllegalStateException("A turma não atingiu o quórum mínimo de 5 alunos matriculados.");
        }

        // Rule: Privacy & Uniqueness via HMAC-SHA256 audit_hash
        String auditHash = computeHmacSha256(currentUser.getEmail(), String.valueOf(request.getClassId()));
        if (reviewRepository.existsByAuditHash(auditHash)) {
            throw new IllegalStateException("Você já submeteu uma avaliação para esta turma.");
        }

        String displayName = request.isAnonymous() ? "Estudante Anônimo" : (request.getAuthorDisplayName() != null ? request.getAuthorDisplayName() : currentUser.getName());

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

        return savedReview;
    }

    @Transactional
    public Review upvoteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada"));
        review.setUsefulCount(review.getUsefulCount() + 1);
        return reviewRepository.save(review);
    }

    public Page<Review> getReviewsByProfessor(Long professorId, Pageable pageable) {
        return reviewRepository.findByProfessorId(professorId, pageable);
    }

    public Page<Review> getReviewsByCourse(Long courseId, Pageable pageable) {
        return reviewRepository.findByCourseId(courseId, pageable);
    }

    private void updateProfessorStats(Long professorId) {
        List<Review> reviews = reviewRepository.findByProfessorId(professorId);
        if (reviews.isEmpty()) return;

        double avgRating = reviews.stream().mapToDouble(Review::getOverallRating).average().orElse(0.0);
        long recommendCount = reviews.stream().filter(Review::getRecommend).count();
        double recommendPct = ((double) recommendCount / reviews.size()) * 100.0;

        Professor professor = professorRepository.findById(professorId).orElseThrow();
        professor.setAverageRating(Math.round(avgRating * 100.0) / 100.0);
        professor.setTotalReviews(reviews.size());
        professor.setRecommendationPercentage(Math.round(recommendPct * 10.0) / 10.0);
        professorRepository.save(professor);
    }

    private String computeHmacSha256(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(PEPPER.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal((data + key).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular hash de anonimato", e);
        }
    }
}
