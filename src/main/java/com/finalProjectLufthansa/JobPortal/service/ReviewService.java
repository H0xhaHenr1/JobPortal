package com.finalProjectLufthansa.JobPortal.service;

import com.finalProjectLufthansa.JobPortal.mapper.ReviewMapper;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.Review;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.repository.JobPostingRepository;
import com.finalProjectLufthansa.JobPortal.repository.ReviewRepository;
import com.finalProjectLufthansa.JobPortal.resource.ReviewResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final JobPostingService jobPostingService;
    private final ReviewRepository reviewRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(JobPostingService jobPostingService, ReviewRepository reviewRepository, JobPostingRepository jobPostingRepository, ReviewMapper reviewMapper) {
        this.jobPostingService = jobPostingService;
        this.reviewRepository = reviewRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.reviewMapper = reviewMapper;
    }

    public Review addReview(Long jobId, Long employerId, ReviewResource reviewResource) {
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);

        if (jobPosting.isEmpty() || !jobPosting.get().getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("Job not found or employer does not own this job");
        }
        Review review = reviewMapper.toEntity(reviewResource);
        review.setJob(jobPosting.get());
        review.setEmployer(jobPosting.get().getEmployer());

        Review savedReview = reviewRepository.save(review);
        jobPostingService.updateJobRating(jobId, reviewResource);

        return savedReview;
    }

    public Page<ReviewResource> getReviews(Optional<User> employer, Long jobId, int page, int size, Double rating) {
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);
        if (employer.isEmpty()){throw new RuntimeException("Employer does not exist");}
        if (jobPosting.isEmpty() || !jobPosting.get().getEmployer().getId().equals(employer.get().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found or employer does not own this job");
        }
        List<ReviewResource> reviewResources = reviewRepository.
                findAllByJobAndEmployerAndRating(jobPosting.get(),
                employer.get(), rating).
                stream().
                map(reviewMapper::toReviewResource).
                collect(Collectors.toList());
        System.out.println(reviewResources);
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), reviewResources.size());
        List<ReviewResource> pageContent = reviewResources.subList(start, end);
        return new PageImpl<>(pageContent, pageable, reviewResources.size());

    }
}
