package com.finalProjectLufthansa.JobPortal.repository;

import com.finalProjectLufthansa.JobPortal.model.entity.JobApplication;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.model.enums.ApplicationStatus;
import com.finalProjectLufthansa.JobPortal.resource.JobApplicationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByApplicantAndJob(User applicant, JobPosting job);

    List<JobApplication> findByApplicant(User applicant);

    List<JobApplication> findByJobAndStatus(JobPosting job, ApplicationStatus status);

    List<JobApplication> findByJob(JobPosting jobPosting);


}
