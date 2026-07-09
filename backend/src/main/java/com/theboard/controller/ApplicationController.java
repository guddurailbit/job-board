package com.theboard.controller;


import com.theboard.dto.ApplyRequest;
import com.theboard.model.JobApplication;
import com.theboard.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/jobs/{jobId}/applications")
public class ApplicationController {


    private final JobService jobService;


    public ApplicationController(JobService jobService) {

        this.jobService = jobService;

    }

    @PostMapping
    public ResponseEntity<JobApplication> apply(

            @PathVariable Long jobId,

            @Valid
            @RequestBody ApplyRequest request,

            Authentication authentication

    ) {


        JobApplication application =
                jobService.applyToJob(
                        jobId,
                        request,
                        authentication
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(application);

    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getApplications(

            @PathVariable Long jobId,

            Authentication authentication

    ) {


        return ResponseEntity.ok(

                jobService.applicants(
                        jobId,
                        authentication
                )

        );

    }

}