package com.classdoor.backend.controller;

import com.classdoor.backend.model.Course;
import com.classdoor.backend.model.Professor;
import com.classdoor.backend.repository.CourseRepository;
import com.classdoor.backend.repository.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CatalogController {

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    public CatalogController(ProfessorRepository professorRepository, CourseRepository courseRepository) {
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/home/featured")
    public ResponseEntity<Map<String, Object>> getFeatured() {
        List<Professor> topProfessors = professorRepository.findTop5ByOrderByAverageRatingDesc();
        List<Course> popularCourses = courseRepository.findTop5ByOrderByTotalReviewsDesc();

        Map<String, Object> response = new HashMap<>();
        response.put("topProfessors", topProfessors);
        response.put("popularCourses", popularCourses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professors")
    public ResponseEntity<Page<Professor>> getProfessors(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minRating,
            Pageable pageable
    ) {
        Page<Professor> professors = professorRepository.findWithFilters(search, department, minRating, pageable);
        return ResponseEntity.ok(professors);
    }

    @GetMapping("/professors/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        return professorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/courses")
    public ResponseEntity<Page<Course>> getCourses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minRating,
            Pageable pageable
    ) {
        Page<Course> courses = courseRepository.findWithFilters(search, department, minRating, pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
