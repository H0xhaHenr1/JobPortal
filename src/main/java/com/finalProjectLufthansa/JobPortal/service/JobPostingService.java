package com.finalProjectLufthansa.JobPortal.service;

import com.finalProjectLufthansa.JobPortal.mapper.JobPostingMapper;
import com.finalProjectLufthansa.JobPortal.mapper.ReviewMapper;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.Review;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.repository.JobPostingRepository;
import com.finalProjectLufthansa.JobPortal.repository.UserRepository;
import com.finalProjectLufthansa.JobPortal.resource.JobApplicationResource;
import com.finalProjectLufthansa.JobPortal.resource.JobPostingResource;
import com.finalProjectLufthansa.JobPortal.resource.ReviewResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final JobPostingMapper jobPostingMapper;
    private final ReviewMapper reviewMapper;

    public JobPostingService(JobPostingMapper jobPostingMapper, JobPostingRepository jobPostingRepository, UserRepository userRepository, ReviewMapper reviewMapper) {
        this.jobPostingRepository = jobPostingRepository;
        this.userRepository = userRepository;
        this.jobPostingMapper = jobPostingMapper;
        this.reviewMapper = reviewMapper;
    }

    public void saveJob(JobPostingResource jobPostingResource, User user) {
        JobPosting jobPosting = jobPostingMapper.toEntity(jobPostingResource, user);
        jobPostingRepository.save(jobPosting);
    }

    public Page<JobPostingResource> getAllJobPostings(int page, int size, String location, String jobTitle, String employerName) {
        location = (location != null && !location.isBlank()) ? location : null;
        jobTitle = (jobTitle != null && !jobTitle.isBlank()) ? jobTitle : null;
        employerName = (employerName != null && !employerName.isBlank()) ? employerName : null;
        List<JobPostingResource> jobPostingResourceList = jobPostingRepository.searchJobs(location, jobTitle, employerName).stream().map(jobPostingMapper::toJobPostingResource).toList();

        return toPage(page, size, jobPostingResourceList);
    }

    public Page<JobPostingResource> getEmployerJobs(Optional<User> employer, int page, int size, String location, String jobTitle) {
        if (employer.isEmpty()) {
            throw new RuntimeException("Employer not found");
        }
        location = (location != null && !location.isBlank()) ? location : null;
        jobTitle = (jobTitle != null && !jobTitle.isBlank()) ? jobTitle : null;
        List<JobPostingResource> jobPostingResourceList = jobPostingRepository.findByEmployerAndFilters(employer.get(), location, jobTitle).stream().map(jobPostingMapper::toJobPostingResource).toList();

        return toPage(page, size, jobPostingResourceList);
    }

    //Helper Method
    private Page<JobPostingResource> toPage(int page, int size, List<JobPostingResource> jobPostingResourceList) {
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), jobPostingResourceList.size());
        List<JobPostingResource> pageContent = jobPostingResourceList.subList(start, end);
        return new PageImpl<>(pageContent, pageable, jobPostingResourceList.size());
    }

    public void updateJobRating(Long jobId, ReviewResource reviewResource) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<Review> reviews = jobPosting.getReview();

        if (reviews.isEmpty()) {
            jobPosting.setRating(0.0);
            jobPosting.setNumRatings(0);
        } else {
            double sum = reviews.stream().mapToDouble(Review::getRating).sum();
            jobPosting.setNumRatings(reviews.size());
            jobPosting.setRating(sum / reviews.size());
        }

        jobPostingRepository.save(jobPosting);
    }



}
