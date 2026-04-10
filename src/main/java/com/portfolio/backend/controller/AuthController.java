package com.portfolio.backend.controller;
 
import com.portfolio.backend.dto.LoginRequest;
import com.portfolio.backend.dto.LoginResponse;
import com.portfolio.backend.dto.RegisterRequest;
import com.portfolio.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	 
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
 
 // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
