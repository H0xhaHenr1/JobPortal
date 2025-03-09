package com.finalProjectLufthansa.JobPortal.controller;


import com.finalProjectLufthansa.JobPortal.mapper.JobPostingMapper;
import com.finalProjectLufthansa.JobPortal.mapper.UserMapper;
import com.finalProjectLufthansa.JobPortal.model.entity.JobApplication;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.repository.UserRepository;
import com.finalProjectLufthansa.JobPortal.resource.JobApplicationResource;
import com.finalProjectLufthansa.JobPortal.resource.JobPostingResource;
import com.finalProjectLufthansa.JobPortal.service.JobApplicationService;
import com.finalProjectLufthansa.JobPortal.service.JobPostingService;
import com.finalProjectLufthansa.JobPortal.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/jobseeker")
@PreAuthorize("hasRole('JOB_SEEKER')")
public class JobSeekerController {

    private final JobApplicationService jobApplicationService;
    UserService userService;
    JobPostingService jobPostingService;
    JobPostingMapper jobPostingMapper;

    public JobSeekerController(UserService userService, JobPostingService jobPostingService, JobPostingMapper jobPostingMapper, JobApplicationService jobApplicationService) {
        this.userService = userService;
        this.jobPostingService = jobPostingService;
        this.jobPostingMapper = jobPostingMapper;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<Page<JobPostingResource>> getEmployerJobs(
            Authentication authentication,
            String location,
            String jobTitle,
            String employerName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(jobPostingService.getAllJobPostings(page, size, location, jobTitle, employerName));
    }


    @GetMapping("/applications")
    public ResponseEntity<Page<JobApplicationResource>> getJobSeekerApplications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok(jobApplicationService.getUserApplications(authentication.getName(), page, size));
    }

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<String> applyJob(@PathVariable Long jobId,
                                           @RequestParam("resume") MultipartFile resumeFile,
                                           @RequestParam("diploma") MultipartFile diplomaFile,
                                           Authentication authentication) throws IOException {
        jobApplicationService.applyForJob(authentication.getName(),jobId, resumeFile, diplomaFile);
        return ResponseEntity.ok("Job application applied");
    }
}
