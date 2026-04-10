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
 
    public LoginResponse register(RegisterRequest request) {

        // 1. Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2. Check username not already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists. Please choose another.");
        }

        // 3. Determine role — if username contains "admin" → admin, else student
        String username = request.getUsername().trim();
        String role = username.toLowerCase().contains("admin") ? "admin" : "student";

        // 4. Generate studentId for students (matches your frontend logic)
        long userCount = userRepository.count();
        String studentId = null;
        if (role.equals("student")) {
            studentId = "240003" + String.format("%03d", userCount + 1);
        }

        // 5. Save new user to database
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(request.getPassword());
        newUser.setName(username); // name = username by default
        newUser.setRole(role);
        newUser.setStudentId(studentId);
        newUser.setEmail(username + "@example.com");
        userRepository.save(newUser);

        // 6. Auto-login after register — return token + user
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
