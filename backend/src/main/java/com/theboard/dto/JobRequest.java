package com.theboard.dto;

import jakarta.validation.constraints.NotBlank;

public class JobRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String company;

    @NotBlank
    private String location;

    @NotBlank
    private String type;

    @NotBlank
    private String mode;

    @NotBlank
    private String schedule;

    private Long salary;

    private String tags;

    @NotBlank
    private String description;

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
}
