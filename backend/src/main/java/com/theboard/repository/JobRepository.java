package com.theboard.repository;

import com.theboard.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByEmployerId(Long employerId);

    Page<Job> findAll(Pageable pageable);

}