package com.classdoor.backend.repository;

import com.classdoor.backend.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
           "(:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:department IS NULL OR c.department = :department) AND " +
           "(:minRating IS NULL OR c.averageRating >= :minRating)")
    Page<Course> findWithFilters(
            @Param("search") String search,
            @Param("department") String department,
            @Param("minRating") Double minRating,
            Pageable pageable
    );

    List<Course> findTop5ByOrderByTotalReviewsDesc();
}
