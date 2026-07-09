package com.theboard.service;

import com.theboard.dto.ApplyRequest;
import com.theboard.dto.JobRequest;
import com.theboard.model.Job;
import com.theboard.model.JobApplication;
import com.theboard.model.User;
import com.theboard.repository.JobApplicationRepository;
import com.theboard.repository.JobRepository;
import com.theboard.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {


    private final JobRepository jobRepository;

    private final JobApplicationRepository applicationRepository;

    private final UserRepository userRepository;



    public JobService(
            JobRepository jobRepository,
            JobApplicationRepository applicationRepository,
            UserRepository userRepository
    ) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public List<Job> findJobs(
            String search,
            String location,
            String type,
            String schedule,
            String mode,
            String sort
    ) {


        List<Job> jobs = jobRepository.findAll();


        String q = search == null ? "" :
                search.toLowerCase();


        return jobs.stream()

                .filter(job ->
                        q.isEmpty()
                                ||
                                job.getTitle()
                                        .toLowerCase()
                                        .contains(q)

                                ||

                                job.getCompany()
                                        .toLowerCase()
                                        .contains(q)
                )

                .filter(job ->
                        location == null
                                ||
                                location.isEmpty()
                                ||
                                job.getLocation()
                                        .toLowerCase()
                                        .contains(location.toLowerCase())
                )

                .sorted(
                        Comparator.comparing(
                                Job::getPostedAt
                        ).reversed()
                )

                .toList();

    }



    public Optional<Job> findById(Long id){

        return jobRepository.findById(id);

    }


    public Job createJob(
            JobRequest request,
            Authentication authentication
    ){


        User employer =
                getLoggedUser(authentication);



        Job job = new Job();


        job.setTitle(request.getTitle());

        job.setCompany(request.getCompany());

        job.setLocation(request.getLocation());

        job.setType(request.getType());

        job.setMode(request.getMode());

        job.setSchedule(request.getSchedule());

        job.setSalary(request.getSalary());

        job.setTags(request.getTags());

        job.setDescription(request.getDescription());


        job.setEmployer(employer);



        return jobRepository.save(job);

    }


    public List<Job> getMyJobs(
            Authentication authentication
    ){

        User employer =
                getLoggedUser(authentication);


        return jobRepository
                .findByEmployerId(employer.getId());

    }


    public Job updateJob(
            Long id,
            JobRequest request,
            Authentication authentication
    ){


        User employer =
                getLoggedUser(authentication);



        Job job =
                jobRepository.findById(id)

                        .orElseThrow(
                                () ->
                                        new RuntimeException("Job not found")
                        );



        if(!job.getEmployer()
                .getId()
                .equals(employer.getId())){


            throw new RuntimeException(
                    "You cannot edit this job"
            );

        }



        job.setTitle(request.getTitle());

        job.setCompany(request.getCompany());

        job.setLocation(request.getLocation());

        job.setType(request.getType());

        job.setMode(request.getMode());

        job.setSchedule(request.getSchedule());

        job.setSalary(request.getSalary());

        job.setTags(request.getTags());

        job.setDescription(request.getDescription());



        return jobRepository.save(job);

    }


    public void deleteJob(
            Long id,
            Authentication authentication
    ){


        User employer =
                getLoggedUser(authentication);



        Job job =
                jobRepository.findById(id)

                        .orElseThrow(
                                () ->
                                        new RuntimeException("Job not found")
                        );



        if(!job.getEmployer()
                .getId()
                .equals(employer.getId())){


            throw new RuntimeException(
                    "You cannot delete this job"
            );

        }



        jobRepository.delete(job);

    }


    public JobApplication applyToJob(
            Long jobId,
            ApplyRequest request,
            Authentication authentication
    ){


        User candidate =
                getLoggedUser(authentication);



        Job job =
                jobRepository.findById(jobId)

                        .orElseThrow(
                                () ->
                                        new RuntimeException("Job not found")
                        );



        JobApplication application =
                new JobApplication();


        application.setJob(job);

        application.setCandidate(candidate);

        application.setResumeLink(
                request.getResumeLink()
        );

        application.setNote(
                request.getNote()
        );


        return applicationRepository.save(application);

    }



    public List<JobApplication> applicants(
            Long jobId,
            Authentication authentication
    ){


        User employer =
                getLoggedUser(authentication);



        Job job =
                jobRepository.findById(jobId)

                        .orElseThrow();



        if(!job.getEmployer()
                .getId()
                .equals(employer.getId())){


            throw new RuntimeException(
                    "Access denied"
            );

        }



        return applicationRepository
                .findByJobId(jobId);

    }





    private User getLoggedUser(
            Authentication authentication
    ){


        return userRepository
                .findByEmail(
                        authentication.getName()
                )

                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "User not found"
                                )
                );

    }


}