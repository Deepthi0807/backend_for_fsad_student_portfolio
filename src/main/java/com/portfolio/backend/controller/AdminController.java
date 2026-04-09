package com.portfolio.backend.controller;

import com.portfolio.backend.dto.FeedbackRequest;
import com.portfolio.backend.dto.ProjectResponse;
import com.portfolio.backend.dto.StudentSummary;
import com.portfolio.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	   private final AdminService adminService;
	   
	    public AdminController(AdminService adminService) {
	        this.adminService = adminService;
	    }
	 
	    // GET /api/admin/students
	    // Called by AdminDashboard.jsx → adminAPI.getStudents()
	    @GetMapping("/students")
	    public ResponseEntity<List<StudentSummary>> getAllStudents() {
	        return ResponseEntity.ok(adminService.getAllStudents());
	    }
	 
	    // GET /api/admin/students/{studentId}/projects
	    // Called by AdminDashboard.jsx → adminAPI.getStudentProjects(studentId)
	    @GetMapping("/students/{studentId}/projects")
	    public ResponseEntity<List<ProjectResponse>> getStudentProjects(
	            @PathVariable String studentId) {
	        return ResponseEntity.ok(adminService.getStudentProjects(studentId));
	    }
	 
	    // POST /api/admin/projects/{projectId}/feedback
	    // Called by AdminDashboard.jsx → adminAPI.addFeedback(projectId, comment)
	    @PostMapping("/projects/{projectId}/feedback")
	    public ResponseEntity<?> addFeedback(
	            @PathVariable Long projectId,
	            @RequestBody FeedbackRequest request,
	            @RequestHeader("Authorization") String authHeader) {
	 
	        Long adminId = extractUserIdFromToken(authHeader);
	        adminService.addFeedback(projectId, request, adminId);
	        return ResponseEntity.ok(Map.of("message", "Feedback saved successfully"));
	    }
	 
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
    
}
