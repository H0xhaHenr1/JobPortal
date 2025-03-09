package com.finalProjectLufthansa.JobPortal.controller;


import com.finalProjectLufthansa.JobPortal.mapper.JobPostingMapper;
import com.finalProjectLufthansa.JobPortal.model.entity.JobApplication;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.Review;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.model.enums.ApplicationStatus;
import com.finalProjectLufthansa.JobPortal.repository.JobPostingRepository;
import com.finalProjectLufthansa.JobPortal.repository.ReviewRepository;
import com.finalProjectLufthansa.JobPortal.resource.JobApplicationResource;
import com.finalProjectLufthansa.JobPortal.resource.JobPostingResource;
import com.finalProjectLufthansa.JobPortal.resource.ReviewResource;
import com.finalProjectLufthansa.JobPortal.service.JobApplicationService;
import com.finalProjectLufthansa.JobPortal.service.JobPostingService;
import com.finalProjectLufthansa.JobPortal.service.ReviewService;
import com.finalProjectLufthansa.JobPortal.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/employer")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerController {

    private final JobPostingService jobPostingService;
    private final JobPostingMapper jobPostingMapper;
    private final JobApplicationService jobApplicationService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final JobPostingRepository jobPostingRepository;
    private final ReviewRepository reviewRepository;

    public EmployerController(
            JobPostingService jobPostingService,
            JobPostingMapper jobPostingMapper,
            JobApplicationService jobApplicationService,
            ReviewService reviewService,
            UserService userService, JobPostingRepository jobPostingRepository, ReviewRepository reviewRepository) {
        this.jobPostingService = jobPostingService;
        this.jobPostingMapper = jobPostingMapper;
        this.jobApplicationService = jobApplicationService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.jobPostingRepository = jobPostingRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/jobs")
    public ResponseEntity<?> postJob(
            @RequestBody  JobPostingResource jobPostingResource,
            Authentication authentication) {
        Optional<User> employer = userService.getUserByUsername(authentication.getName());
        if (employer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: Employer not found.");
        }
        if (jobPostingResource.title().isEmpty() || jobPostingResource.description().isEmpty() || jobPostingResource.requirements().isEmpty() || jobPostingResource.location().isEmpty()) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fill all the required fields.");
        }else if(jobPostingRepository.findByTitle(jobPostingResource.title()).getEmployer().getUsername().equals(authentication.getName())){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Job already exists.");
        } {
        }
        jobPostingService.saveJob(jobPostingResource, employer.get());
        return ResponseEntity.status(HttpStatus.CREATED).body("Job created successfully.");

    }

    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<Page<JobApplicationResource>> getJobApplications(
            @PathVariable Long jobId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ApplicationStatus status) {
        Optional<User> employer = userService.getUserByUsername(authentication.getName());
        Page<JobApplicationResource> applications = jobApplicationService.getEmployerJobApplications(size,page, jobId, employer.get().getId(),status);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/jobs")
    public ResponseEntity<Page<JobPostingResource>> getEmployerJobs(
            Authentication authentication,
            String location,
            String jobTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Optional<User> employer = userService.getUserByUsername(authentication.getName());
        Page<JobPostingResource> jobResources = jobPostingService.getEmployerJobs(employer, page, size, location, jobTitle);

        return ResponseEntity.ok(jobResources);
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus newStatus,
            Authentication authentication) {

        Optional<User> employer = userService.getUserByUsername(authentication.getName());
        jobApplicationService.updateApplicationStatus(applicationId, newStatus, employer.get().getId());
        return ResponseEntity.noContent().build();
    }

    // ✅ ADD Review for a Job
    @PostMapping("/jobs/{jobId}/reviews")
    public ResponseEntity<String> addReview(
            @PathVariable Long jobId,
            @RequestBody ReviewResource review,
            Authentication authentication) {

        Optional<User> employer = userService.getUserByUsername(authentication.getName());
        Review savedReview = reviewService.addReview(jobId, employer.get().getId(), review);
        return ResponseEntity.status(HttpStatus.CREATED).body("Review created successfully.");
    }

    @GetMapping("/jobs/{jobId}/reviews")
    public ResponseEntity<Page<ReviewResource>> getReviews(@PathVariable Long jobId,
                                                           Double rating,
                                                           Authentication authentication,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Optional<User> employer = userService.getUserByUsername(authentication.getName());
        Page<ReviewResource> reviewResources = reviewService.getReviews(employer, jobId, page, size, rating);
        return ResponseEntity.ok(reviewResources);
    }
}

