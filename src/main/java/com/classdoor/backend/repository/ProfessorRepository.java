package com.classdoor.backend.repository;

import com.classdoor.backend.model.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("SELECT p FROM Professor p WHERE " +
           "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:department IS NULL OR p.department = :department) AND " +
           "(:minRating IS NULL OR p.averageRating >= :minRating)")
    Page<Professor> findWithFilters(
            @Param("search") String search,
            @Param("department") String department,
            @Param("minRating") Double minRating,
            Pageable pageable
    );

    List<Professor> findTop5ByOrderByAverageRatingDesc();
}
