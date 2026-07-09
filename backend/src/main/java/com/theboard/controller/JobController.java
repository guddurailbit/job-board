package com.theboard.controller;


import com.theboard.dto.JobRequest;
import com.theboard.model.Job;
import com.theboard.model.JobApplication;
import com.theboard.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/jobs")
public class JobController {


    private final JobService jobService;


    public JobController(JobService jobService) {

        this.jobService = jobService;

    }

    @GetMapping
    public List<Job> jobs(@RequestParam(required = false) String search, @RequestParam(required = false) String location, @RequestParam(required = false) String type, @RequestParam(required = false) String schedule, @RequestParam(required = false) String mode, @RequestParam(defaultValue = "newest") String sort) {

        return jobService.findJobs(search, location, type, schedule, mode, sort);

    }


    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {

        return jobService.findById(id)

                .map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<Job> create(@Valid @RequestBody JobRequest request, Authentication authentication) {

        return ResponseEntity.ok(

                jobService.createJob(request, authentication)

        );

    }

    @GetMapping("/employer/my-jobs")
    public List<Job> myJobs(Authentication authentication) {

        return jobService.getMyJobs(authentication);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> update(@PathVariable Long id, @RequestBody JobRequest request, Authentication authentication) {

        return ResponseEntity.ok(jobService.updateJob(id, request, authentication));

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {

        jobService.deleteJob(id, authentication);


        return ResponseEntity.ok("Job deleted");

    }


    @GetMapping("/{id}/applications")
    public List<JobApplication> applicants(@PathVariable Long id, Authentication authentication) {

        return jobService.applicants(id, authentication);

    }

}