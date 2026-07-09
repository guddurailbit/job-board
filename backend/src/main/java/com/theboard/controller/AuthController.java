package com.theboard.controller;


import com.theboard.dto.LoginRequest;
import com.theboard.dto.LoginResponse;
import com.theboard.dto.RegisterRequest;
import com.theboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ===========================
    // Register
    // ===========================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        try {

            String message = userService.register(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(message);

        } catch (RuntimeException ex) {

            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());

        }
    }

    // ===========================
    // Login
    // ===========================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {

            LoginResponse response = userService.login(request);

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ex.getMessage());

        }
    }

}