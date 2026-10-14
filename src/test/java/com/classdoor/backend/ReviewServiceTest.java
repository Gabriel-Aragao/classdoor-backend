package com.classdoor.backend;

import com.classdoor.backend.dto.ReviewRequest;
import com.classdoor.backend.model.ClassRoom;
import com.classdoor.backend.model.Professor;
import com.classdoor.backend.model.Role;
import com.classdoor.backend.model.User;
import com.classdoor.backend.repository.ClassRoomRepository;
import com.classdoor.backend.repository.ProfessorRepository;
import com.classdoor.backend.repository.ReviewRepository;
import com.classdoor.backend.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ClassRoomRepository classRoomRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitReviewSuccess() {
        User student = new User("Student", "student@university.edu", "password", Role.ROLE_STUDENT);
        student.setId(1L);

        ClassRoom classRoom = new ClassRoom(10L, 1L, "2026.1", true, "ANONYMOUS_ONLY", 10);
        classRoom.setId(5L);

        ReviewRequest request = new ReviewRequest();
        request.setProfessorId(1L);
        request.setCourseId(10L);
        request.setClassId(5L);
        request.setOverallRating(5.0);
        request.setDifficultyRating(3.0);
        request.setRecommend(true);
        request.setComment("Excelente professor, didática maravilhosa e suporte total.");
        request.setAnonymous(true);

        when(classRoomRepository.findById(5L)).thenReturn(Optional.of(classRoom));
        when(reviewRepository.existsByAuditHash(anyString())).thenReturn(false);
        when(reviewRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(new Professor()));

        var saved = reviewService.submitReview(request, student);

        assertNotNull(saved);
        assertEquals(5.0, saved.getOverallRating());
        assertEquals("Estudante Anônimo", saved.getAuthorDisplayName());
        assertTrue(saved.isAnonymous());
    }

    @Test
    void testSubmitReviewEvaluationClosed() {
        User student = new User("Student", "student@university.edu", "password", Role.ROLE_STUDENT);
        ClassRoom classRoom = new ClassRoom(10L, 1L, "2026.1", false, "ANONYMOUS_ONLY", 10);
        classRoom.setId(5L);

        ReviewRequest request = new ReviewRequest();
        request.setClassId(5L);

        when(classRoomRepository.findById(5L)).thenReturn(Optional.of(classRoom));

        assertThrows(IllegalStateException.class, () -> reviewService.submitReview(request, student));
    }

    @Test
    void testSubmitReviewQuorumNotMet() {
        User student = new User("Student", "student@university.edu", "password", Role.ROLE_STUDENT);
        ClassRoom classRoom = new ClassRoom(10L, 1L, "2026.1", true, "ANONYMOUS_ONLY", 3); // < 5 quorum
        classRoom.setId(5L);

        ReviewRequest request = new ReviewRequest();
        request.setClassId(5L);

        when(classRoomRepository.findById(5L)).thenReturn(Optional.of(classRoom));

        assertThrows(IllegalStateException.class, () -> reviewService.submitReview(request, student));
    }
}
