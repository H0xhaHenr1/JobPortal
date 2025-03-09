package com.finalProjectLufthansa.JobPortal.mapper;

import com.finalProjectLufthansa.JobPortal.model.entity.JobApplication;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.resource.JobApplicationResource;
import com.finalProjectLufthansa.JobPortal.resource.JobPostingResource;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {
    public JobApplicationResource toJobApplicationResource(final JobApplication jobApplication) {
        return new JobApplicationResource(
                jobApplication.getApplicant().getName(),
                jobApplication.getApplicant().getLastname(),
                jobApplication.getJobPosting().getTitle(),
                jobApplication.getResumeUrl(),
                jobApplication.getDiplomaUrl(),
                jobApplication.getStatus()
        );
    }

    public JobApplication toEntity(JobApplicationResource application) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setResumeUrl(application.resumeUrl());
        jobApplication.setDiplomaUrl(application.diplomaUrl());
        jobApplication.setStatus(application.applicationStatus());
        return jobApplication;
    }




}
