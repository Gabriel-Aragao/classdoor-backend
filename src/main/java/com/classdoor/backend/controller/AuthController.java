package com.classdoor.backend.controller;

import com.classdoor.backend.dto.AuthResponse;
import com.classdoor.backend.dto.LoginRequest;
import com.classdoor.backend.dto.RegisterRequest;
import com.classdoor.backend.model.User;
import com.classdoor.backend.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Attempting registration for email: {}", request.getEmail());
        try {
            AuthResponse response = authService.register(request);
            logger.info("Successfully registered user: {} with role: {}", request.getEmail(), request.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.warn("Failed registration attempt for email: {}. Reason: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Attempting authentication (login) for email: {}", request.getEmail());
        try {
            AuthResponse response = authService.login(request);
            logger.info("Successfully authenticated user: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Authentication failed for email: {}. Reason: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            logger.warn("Unauthorized access attempt to /api/auth/me (no authenticated principal)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logger.debug("Fetched current authenticated user profile for: {}", user.getEmail());
        AuthResponse response = new AuthResponse(
                null,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.ok(response);
    }
}
