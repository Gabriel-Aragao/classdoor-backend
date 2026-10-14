package com.classdoor.backend.repository;

import com.classdoor.backend.model.ClassEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, Long> {
    List<ClassEnrollment> findByClassId(Long classId);
    boolean existsByClassIdAndStudentEmail(Long classId, String studentEmail);
}
