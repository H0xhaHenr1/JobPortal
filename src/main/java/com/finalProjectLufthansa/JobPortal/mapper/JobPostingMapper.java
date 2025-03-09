package com.finalProjectLufthansa.JobPortal.mapper;

import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.resource.JobPostingResource;
import com.finalProjectLufthansa.JobPortal.resource.UserResource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JobPostingMapper {
    public JobPostingResource toJobPostingResource(final JobPosting jobPosting) {
        return new JobPostingResource(
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getRequirements(),
                jobPosting.getLocation(),
                jobPosting.getEmployer().getId()
                );
    }

    public JobPosting toEntity(JobPostingResource jobPostingResource, User employer) {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle(jobPostingResource.title());
        jobPosting.setDescription(jobPostingResource.description());
        jobPosting.setRequirements(jobPostingResource.requirements());
        jobPosting.setLocation(jobPostingResource.location());
        jobPosting.setEmployer(employer);
        return jobPosting;
    }


}
