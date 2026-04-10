package com.portfolio.backend.controller;

import com.portfolio.backend.dto.CreateProjectRequest;
import com.portfolio.backend.dto.ProjectResponse;
import com.portfolio.backend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

import java.util.Base64;
import java.util.List;



@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
 
    // GET /api/student/projects
    // Called by StudentDashboard.jsx → studentAPI.getProjects()
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> getProjects(
            @RequestHeader("Authorization") String authHeader) {
 
        Long userId = extractUserIdFromToken(authHeader);
        return ResponseEntity.ok(studentService.getProjects(userId));
    }
 
    // POST /api/student/projects
    // Called by StudentDashboard.jsx → studentAPI.createProject(newProject)
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateProjectRequest request) {
 
        Long userId = extractUserIdFromToken(authHeader);
        ProjectResponse created = studentService.createProject(userId, request);
        return ResponseEntity.ok(created);
    }
 
    // Decode Base64 token → get userId
    // Token format: Base64("userId:role")
    private Long extractUserIdFromToken(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            return Long.parseLong(parts[0]);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or missing token");
        }
    }
 // PUT /api/student/projects/{id}
    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody CreateProjectRequest request) {

        Long userId = extractUserIdFromToken(authHeader);
        ProjectResponse updated = studentService.updateProject(userId, id, request);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/student/projects/{id}
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteProject(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long userId = extractUserIdFromToken(authHeader);
        studentService.deleteProject(userId, id);
        return ResponseEntity.ok(Map.of("message", "Project deleted"));
    }
}
