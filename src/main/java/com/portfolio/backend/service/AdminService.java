package com.portfolio.backend.service;
 
import com.portfolio.backend.dto.FeedbackRequest;
import com.portfolio.backend.dto.ProjectResponse;
import com.portfolio.backend.dto.StudentSummary;
import com.portfolio.backend.model.Feedback;
import com.portfolio.backend.model.Project;
import com.portfolio.backend.model.User;
import com.portfolio.backend.repository.FeedbackRepository;
import com.portfolio.backend.repository.ProjectRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
	private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FeedbackRepository feedbackRepository;
    private final StudentService studentService;

    // ✅ ONLY ONE constructor (manual)
    public AdminService(UserRepository userRepository,
                        ProjectRepository projectRepository,
                        FeedbackRepository feedbackRepository,
                        StudentService studentService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.feedbackRepository = feedbackRepository;
        this.studentService = studentService;
    }

    // GET /api/admin/students
    public List<StudentSummary> getAllStudents() {
        List<User> students = userRepository.findByRole("student");

        return students.stream().map(student -> {
            long total = projectRepository.countByStudentId(student.getId());
            long reviewed = projectRepository.countByStudentIdAndReviewed(student.getId(), true);
            long pending = total - reviewed;

            return new StudentSummary(
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    total,
                    reviewed,
                    pending
            );
        }).collect(Collectors.toList());
    }

    // GET /api/admin/students/{studentId}/projects
    public List<ProjectResponse> getStudentProjects(String studentId) {
        User student = userRepository.findAll().stream()
                .filter(u -> studentId.equals(u.getStudentId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        return projectRepository.findByStudentId(student.getId())
                .stream()
                .map(studentService::toResponse)
                .collect(Collectors.toList());
    }

    // POST /api/admin/projects/{projectId}/feedback
    public void addFeedback(Long projectId, FeedbackRequest request, Long adminUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Feedback feedback = new Feedback();
        feedback.setComment(request.getComment());
        feedback.setProject(project);
        feedback.setAdmin(admin);
        feedbackRepository.save(feedback);

        project.setReviewed(true);
        projectRepository.save(project);
    }
}
