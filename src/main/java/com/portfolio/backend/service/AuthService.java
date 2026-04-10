package com.portfolio.backend.service;

import com.portfolio.backend.dto.LoginRequest;
import com.portfolio.backend.dto.LoginResponse;
import com.portfolio.backend.dto.RegisterRequest;
import com.portfolio.backend.dto.UserInfo;
import com.portfolio.backend.model.User;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
 
import java.util.Base64;
import java.util.Optional;
 
@Service
public class AuthService {
	 private final UserRepository userRepository;

	    public AuthService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }

	    // ── LOGIN ─────────────────────────────────────────────
	    public LoginResponse login(LoginRequest request) {

	        Optional<User> optUser = userRepository.findByUsername(request.getUsername());

	        if (optUser.isEmpty()) {
	            throw new RuntimeException("Invalid username or password");
	        }

	        User user = optUser.get();

	        if (!user.getPassword().equals(request.getPassword())) {
	            throw new RuntimeException("Invalid username or password");
	        }

	        String token = Base64.getEncoder()
	                .encodeToString((user.getId() + ":" + user.getRole()).getBytes());

	        UserInfo userInfo = new UserInfo(
	                user.getId(),
	                user.getName(),
	                user.getRole(),
	                user.getStudentId(),
	                user.getEmail()
	        );

	        return new LoginResponse(token, userInfo);
	    }

	    // ── REGISTER ──────────────────────────────────────────
	    public LoginResponse register(RegisterRequest request) {

	        // 1. Validate all fields present
	        if (request.getName() == null || request.getName().trim().isEmpty()) {
	            throw new RuntimeException("Name is required");
	        }
	        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
	            throw new RuntimeException("Email is required");
	        }

	        // 2. Validate passwords match
	        if (!request.getPassword().equals(request.getConfirmPassword())) {
	            throw new RuntimeException("Passwords do not match");
	        }

	        // 3. Check username not already taken
	        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
	            throw new RuntimeException("Username already exists. Please choose another.");
	        }

	        // 4. Determine role
	        String username = request.getUsername().trim();
	        String role = username.toLowerCase().contains("admin") ? "admin" : "student";

	        // 5. Generate studentId for students
	        String studentId = null;
	        if (role.equals("student")) {
	            long userCount = userRepository.count();
	            studentId = "240003" + String.format("%03d", userCount + 1);
	        }

	        // 6. Save new user
	        User newUser = new User();
	        newUser.setName(request.getName().trim());
	        newUser.setEmail(request.getEmail().trim());
	        newUser.setUsername(username);
	        newUser.setPassword(request.getPassword());
	        newUser.setRole(role);
	        newUser.setStudentId(studentId);
	        userRepository.save(newUser);

	        // 7. Return token + user info
	        String token = Base64.getEncoder()
	                .encodeToString((newUser.getId() + ":" + newUser.getRole()).getBytes());

	        UserInfo userInfo = new UserInfo(
	                newUser.getId(),
	                newUser.getName(),
	                newUser.getRole(),
	                newUser.getStudentId(),
	                newUser.getEmail()
	        );

	        return new LoginResponse(token, userInfo);
	    }
	}