package com.classdoor.backend;

import com.classdoor.backend.controller.CatalogController;
import com.classdoor.backend.model.Course;
import com.classdoor.backend.model.Professor;
import com.classdoor.backend.repository.CourseRepository;
import com.classdoor.backend.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogControllerTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CatalogController catalogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFeatured() {
        when(professorRepository.findTop5ByOrderByAverageRatingDesc()).thenReturn(List.of(new Professor()));
        when(courseRepository.findTop5ByOrderByTotalReviewsDesc()).thenReturn(List.of(new Course()));

        ResponseEntity<Map<String, Object>> response = catalogController.getFeatured();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("topProfessors"));
        assertTrue(response.getBody().containsKey("popularCourses"));
    }

    @Test
    void testGetProfessorByIdFound() {
        Professor prof = new Professor();
        prof.setId(1L);
        prof.setName("Test Prof");
        when(professorRepository.findById(1L)).thenReturn(Optional.of(prof));

        ResponseEntity<Professor> response = catalogController.getProfessorById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Test Prof", response.getBody().getName());
    }

    @Test
    void testGetProfessorByIdNotFound() {
        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Professor> response = catalogController.getProfessorById(99L);

        assertEquals(404, response.getStatusCode().value());
    }
}
