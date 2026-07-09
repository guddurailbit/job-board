package com.theboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    @JsonIgnore
    private User employer;

    @NotBlank
    @Column(nullable = false)
    private String location;

    // Full-time / Part-time / Contract / Internship
    @Column(nullable = false)
    private String type;

    // Onsite / Remote / Hybrid
    @Column(nullable = false)
    private String mode;

    // Day shift / Night shift / Flexible
    @Column(nullable = false)
    private String schedule;

    // Annual salary in INR, 0/null if undisclosed
    private Long salary;

    // comma-separated tags, e.g. "React,TypeScript,CSS"
    @Column(length = 1000)
    private String tags;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime postedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<JobApplication> applications = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (postedAt == null) {
            postedAt = LocalDateTime.now();
        }
    }

    public Job() {}

    // ---- Getters and setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public Long getSalary() { return salary; }
    public void setSalary(Long salary) { this.salary = salary; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

    public List<JobApplication> getApplications() { return applications; }
    public void setApplications(List<JobApplication> applications) { this.applications = applications; }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }
}
