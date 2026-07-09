package com.theboard.config;

import com.theboard.model.Job;
import com.theboard.model.Role;
import com.theboard.model.User;
import com.theboard.repository.JobRepository;
import com.theboard.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class DataSeeder implements CommandLineRunner {


    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public DataSeeder(JobRepository jobRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) {


        if (jobRepository.count() > 0) {
            return;
        }


        User employer = userRepository.findByEmail("hr@northlane.com").orElseGet(() -> {

            User user = new User();

            user.setName("Northlane HR");
            user.setEmail("hr@northlane.com");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole(Role.EMPLOYER);
            user.setCompanyName("Northlane Labs");


            return userRepository.save(user);
        });


        jobRepository.save(job("Frontend Engineer", "Northlane Labs", "Hyderabad, IN", "Full-time", "Onsite", "Night shift", 1200000L, "React,TypeScript,CSS", "Build and maintain customer-facing dashboards.", LocalDateTime.now().minusDays(1), employer));


        jobRepository.save(job("Backend Engineer (Java)", "Ferrous Systems India", "Bengaluru, IN", "Full-time", "Hybrid", "Day shift", 1600000L, "Java,Spring Boot,MySQL", "Design backend services.", LocalDateTime.now().minusDays(3), employer));
        jobRepository.save(job("Backend Engineer (Java)", "Ferrous Systems India", "Bengaluru, IN", "Full-time", "Hybrid", "Day shift", 600000L, "Java,Spring Boot,MySQL", "Design backend services.", LocalDateTime.now().minusDays(3), employer));
        jobRepository.save(job("Backend Engineer (Java)", "Ferrous Systems India", "Bengaluru, IN", "Full-time", "Hybrid", "Day shift", 1000000L, "Java,Spring Boot,MySQL", "Design backend services.", LocalDateTime.now().minusDays(3), employer));
        jobRepository.save(job("Backend Engineer (Java)", "Ferrous Systems India", "Bengaluru, IN", "Full-time", "Hybrid", "Day shift", 1200000L, "Java,Spring Boot,MySQL", "Design backend services.", LocalDateTime.now().minusDays(3), employer));
        jobRepository.save(job("Backend Engineer (Java)", "Ferrous Systems India", "Bengaluru, IN", "Full-time", "Hybrid", "Day shift", 900000L, "Java,Spring Boot,MySQL", "Design backend services.", LocalDateTime.now().minusDays(3), employer));

    }


    private Job job(String title, String company, String location, String type, String mode, String schedule, Long salary, String tags, String description, LocalDateTime postedAt, User employer) {
        Job job = new Job();

        job.setTitle(title);
        job.setCompany(company);
        job.setLocation(location);
        job.setType(type);
        job.setMode(mode);
        job.setSchedule(schedule);
        job.setSalary(salary);
        job.setTags(tags);
        job.setDescription(description);
        job.setPostedAt(postedAt);
        job.setEmployer(employer);


        return job;
    }

}