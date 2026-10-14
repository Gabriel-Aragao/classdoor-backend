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
@RequestMapping("/api/v1/courses")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping("/{cId}/classes")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> createClass(
            @PathVariable Long cId,
            @Valid @RequestBody ClassRoomRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        ClassRoom classRoom = classService.createClass(cId, currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(classRoom);
    }

    @PostMapping("/{cId}/classes/{clId}/students/bulk")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> addStudentsBulk(
            @PathVariable Long cId,
            @PathVariable Long clId,
            @Valid @RequestBody BulkStudentsRequest request
    ) {
        ClassRoom classRoom = classService.addStudentsBulk(clId, request);
        return ResponseEntity.ok(classRoom);
    }

    @PatchMapping("/{cId}/classes/{clId}/toggle-evaluation")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> toggleEvaluation(
            @PathVariable Long cId,
            @PathVariable Long clId
    ) {
        ClassRoom classRoom = classService.toggleEvaluation(clId);
        return ResponseEntity.ok(classRoom);
    }

    @PatchMapping("/{cId}/classes/{clId}/policy")
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR')")
    public ResponseEntity<ClassRoom> updatePolicy(
            @PathVariable Long cId,
            @PathVariable Long clId,
            @RequestBody Map<String, String> body
    ) {
        String policy = body.getOrDefault("policy", "ANONYMOUS_ONLY");
        ClassRoom classRoom = classService.updatePolicy(clId, policy);
        return ResponseEntity.ok(classRoom);
    }
}
