package com.portfolio.backend.repository;

import com.portfolio.backend.model.Project;
import com.portfolio.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
 
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findByStudent(User student);
    List<Project> findByStudentId(Long studentId);
    long countByStudentId(Long studentId);
    long countByStudentIdAndReviewed(Long studentId, Boolean reviewed);
}
