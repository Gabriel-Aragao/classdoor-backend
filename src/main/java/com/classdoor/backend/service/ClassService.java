package com.classdoor.backend.service;

import com.classdoor.backend.dto.BulkStudentsRequest;
import com.classdoor.backend.dto.ClassRoomRequest;
import com.classdoor.backend.model.ClassEnrollment;
import com.classdoor.backend.model.ClassRoom;
import com.classdoor.backend.repository.ClassEnrollmentRepository;
import com.classdoor.backend.repository.ClassRoomRepository;
import com.classdoor.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassService {

    private final ClassRoomRepository classRoomRepository;
    private final ClassEnrollmentRepository classEnrollmentRepository;
    private final ReviewRepository reviewRepository;

    public ClassService(ClassRoomRepository classRoomRepository, ClassEnrollmentRepository classEnrollmentRepository, ReviewRepository reviewRepository) {
        this.classRoomRepository = classRoomRepository;
        this.classEnrollmentRepository = classEnrollmentRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ClassRoom createClass(Long courseId, Long professorId, ClassRoomRequest request) {
        ClassRoom classRoom = new ClassRoom(
                courseId,
                professorId,
                request.getSemester(),
                false, // default evaluation closed
                request.getPolicy(),
                0
        );
        return classRoomRepository.save(classRoom);
    }

    @Transactional
    public ClassRoom addStudentsBulk(Long classId, BulkStudentsRequest request) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

        // Anti-elimination rule: Once reviews > 0, block adding students
        long reviewsCount = reviewRepository.findAll().stream().filter(r -> r.getClassId().equals(classId)).count();
        if (reviewsCount > 0) {
            throw new IllegalStateException("Bloqueado: Não é permitida a inclusão de novos alunos após o registro da primeira avaliação.");
        }

        for (String email : request.getEmails()) {
            if (!classEnrollmentRepository.existsByClassIdAndStudentEmail(classId, email)) {
                classEnrollmentRepository.save(new ClassEnrollment(classId, email));
            }
        }

        List<ClassEnrollment> allEnrollments = classEnrollmentRepository.findByClassId(classId);
        classRoom.setEnrolledStudentsCount(allEnrollments.size());
        return classRoomRepository.save(classRoom);
    }

    @Transactional
    public ClassRoom toggleEvaluation(Long classId) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));
        classRoom.setEvaluationOpen(!classRoom.isEvaluationOpen());
        return classRoomRepository.save(classRoom);
    }

    @Transactional
    public ClassRoom updatePolicy(Long classId, String policy) {
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));
        classRoom.setPolicy(policy);
        return classRoomRepository.save(classRoom);
    }
}
