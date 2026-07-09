package com.theboard.repository;

import com.theboard.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByCandidateId(Long candidateId);
    List<JobApplication> findByJobEmployerId(Long employerId);
    long countByJobEmployerId(Long employerId);
    long countByCandidateId(Long candidateId);
    long countByStatus(String status);
}
