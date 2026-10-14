package com.classdoor.backend.controller;

import com.classdoor.backend.model.Course;
import com.classdoor.backend.model.Professor;
import com.classdoor.backend.model.Review;
import com.classdoor.backend.repository.CourseRepository;
import com.classdoor.backend.repository.ProfessorRepository;
import com.classdoor.backend.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final ReviewRepository reviewRepository;

    public CatalogController(ProfessorRepository professorRepository, CourseRepository courseRepository, ReviewRepository reviewRepository) {
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
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

    @GetMapping("/professors/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getProfessorAnalytics(@PathVariable Long id) {
        Professor professor = professorRepository.findById(id).orElse(null);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }

        List<Review> reviews = reviewRepository.findByProfessorId(id);

        long star5 = reviews.stream().filter(r -> Math.round(r.getOverallRating()) == 5).count();
        long star4 = reviews.stream().filter(r -> Math.round(r.getOverallRating()) == 4).count();
        long star3 = reviews.stream().filter(r -> Math.round(r.getOverallRating()) == 3).count();
        long star2 = reviews.stream().filter(r -> Math.round(r.getOverallRating()) == 2).count();
        long star1 = reviews.stream().filter(r -> Math.round(r.getOverallRating()) == 1).count();

        Map<String, Object> histogram = new HashMap<>();
        histogram.put("5", star5);
        histogram.put("4", star4);
        histogram.put("3", star3);
        histogram.put("2", star2);
        histogram.put("1", star1);

        Map<String, Object> scorecards = new HashMap<>();
        scorecards.put("overallRating", professor.getAverageRating());
        scorecards.put("difficultyRating", reviews.stream().mapToDouble(Review::getDifficultyRating).average().orElse(3.0));
        scorecards.put("recommendationPercentage", professor.getRecommendationPercentage());
        scorecards.put("totalReviews", professor.getTotalReviews());

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("professorId", professor.getId());
        analytics.put("professorName", professor.getName());
        analytics.put("scorecards", scorecards);
        analytics.put("histogram", histogram);

        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/professors/{id}/analytics/export")
    public ResponseEntity<String> exportAnalytics(
            @PathVariable Long id,
            @RequestParam(defaultValue = "csv") String format
    ) {
        Professor professor = professorRepository.findById(id).orElse(null);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }

        if ("csv".equalsIgnoreCase(format)) {
            StringBuilder csv = new StringBuilder();
            csv.append("Professor ID,Professor Name,Department,Average Rating,Total Reviews,Recommendation %\n");
            csv.append(professor.getId()).append(",")
               .append("\"").append(professor.getName()).append("\",")
               .append("\"").append(professor.getDepartment()).append("\",")
               .append(professor.getAverageRating()).append(",")
               .append(professor.getTotalReviews()).append(",")
               .append(professor.getRecommendationPercentage()).append("\n");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "professor-" + id + "-analytics.csv");
            return ResponseEntity.ok().headers(headers).body(csv.toString());
        }

        return ResponseEntity.badRequest().body("Unsupported export format: " + format);
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
