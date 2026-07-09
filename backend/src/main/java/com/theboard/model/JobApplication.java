package com.theboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


@Entity
@Table(name="job_applications")
public class JobApplication {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="job_id", nullable=false)
    @JsonIgnoreProperties({"applications"})
    private Job job;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="candidate_id")
    @JsonIgnoreProperties({"password"})
    private User candidate;



    @NotBlank
    private String resumeLink;



    @Column(columnDefinition="TEXT")
    private String note;



    @Column(columnDefinition="TEXT")
    private String employerRemark;



    @Column(nullable=false)
    private String status="PENDING";



    @Column(nullable=false, updatable=false)
    private LocalDateTime submittedAt;



    @PrePersist
    public void prePersist(){

        if(submittedAt==null){
            submittedAt=LocalDateTime.now();
        }

    }



    public JobApplication(){}



    public Long getId(){
        return id;
    }


    public void setId(Long id){
        this.id=id;
    }



    public Job getJob(){
        return job;
    }


    public void setJob(Job job){
        this.job=job;
    }



    public User getCandidate(){
        return candidate;
    }


    public void setCandidate(User candidate){
        this.candidate=candidate;
    }



    public String getResumeLink(){
        return resumeLink;
    }


    public void setResumeLink(String resumeLink){
        this.resumeLink=resumeLink;
    }



    public String getNote(){
        return note;
    }


    public void setNote(String note){
        this.note=note;
    }



    public String getEmployerRemark(){
        return employerRemark;
    }


    public void setEmployerRemark(String employerRemark){
        this.employerRemark=employerRemark;
    }



    public String getStatus(){
        return status;
    }


    public void setStatus(String status){
        this.status=status;
    }



    public LocalDateTime getSubmittedAt(){
        return submittedAt;
    }


    public void setSubmittedAt(LocalDateTime submittedAt){
        this.submittedAt=submittedAt;
    }

}