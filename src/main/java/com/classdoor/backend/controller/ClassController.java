package com.classdoor.backend.controller;

import com.classdoor.backend.dto.BulkStudentsRequest;
import com.classdoor.backend.dto.ClassRoomRequest;
import com.classdoor.backend.model.ClassRoom;
import com.classdoor.backend.model.User;
import com.classdoor.backend.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/course/{cId}")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> createClass(
            @PathVariable Long cId,
            @Valid @RequestBody ClassRoomRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        ClassRoom classRoom = classService.createClass(cId, currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(classRoom);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> createClassDefault(
            @Valid @RequestBody ClassRoomRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        // Fallback if courseId is passed in request body or default to 1L
        Long courseId = 1L;
        ClassRoom classRoom = classService.createClass(courseId, currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(classRoom);
    }

    @PostMapping("/{clId}/students")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> addStudentsBulk(
            @PathVariable Long clId,
            @Valid @RequestBody BulkStudentsRequest request
    ) {
        ClassRoom classRoom = classService.addStudentsBulk(clId, request);
        return ResponseEntity.ok(classRoom);
    }

    @PatchMapping("/{clId}/evaluation-status")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> toggleEvaluation(
            @PathVariable Long clId
    ) {
        ClassRoom classRoom = classService.toggleEvaluation(clId);
        return ResponseEntity.ok(classRoom);
    }

    @PatchMapping("/{clId}/policy")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> updatePolicy(
            @PathVariable Long clId,
            @RequestBody Map<String, String> body
    ) {
        String policy = body.getOrDefault("policy", "ANONYMOUS_ONLY");
        ClassRoom classRoom = classService.updatePolicy(clId, policy);
        return ResponseEntity.ok(classRoom);
    }
}
