package com.portfolio.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String milestone;

    private Integer progress;

    private String media;

    @Column(nullable = false)
    private Boolean reviewed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (reviewed == null) reviewed = false;
        if (progress == null) progress = 0;
    }

    public Project() {}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getMilestone() { return milestone; }
    public Integer getProgress() { return progress; }
    public String getMedia() { return media; }
    public Boolean getReviewed() { return reviewed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getStudent() { return student; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setMilestone(String milestone) { this.milestone = milestone; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public void setMedia(String media) { this.media = media; }
    public void setReviewed(Boolean reviewed) { this.reviewed = reviewed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setStudent(User student) { this.student = student; }
}
