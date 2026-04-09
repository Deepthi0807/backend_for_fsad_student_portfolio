package com.portfolio.backend.service;

import com.portfolio.backend.dto.CreateProjectRequest;
import com.portfolio.backend.dto.ProjectResponse;
import com.portfolio.backend.model.Project;
import com.portfolio.backend.model.User;
import com.portfolio.backend.repository.ProjectRepository;
import com.portfolio.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

	private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // ✅ Only one constructor
    public StudentService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectResponse> getProjects(Long studentUserId) {
        List<Project> projects = projectRepository.findByStudentId(studentUserId);
        return projects.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse createProject(Long studentUserId, CreateProjectRequest request) {
        User student = userRepository.findById(studentUserId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setMilestone(request.getMilestone() != null ? request.getMilestone() : "Planning");
        project.setProgress(request.getProgress() != null ? request.getProgress() : 0);
        project.setMedia(request.getMedia() != null ? request.getMedia() : "No media uploaded");
        project.setReviewed(false);
        project.setStudent(student);

        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                String.valueOf(project.getId()),
                project.getTitle(),
                project.getDescription(),
                project.getMilestone(),
                project.getProgress(),
                project.getMedia(),
                project.getReviewed()
        );
    }
}
