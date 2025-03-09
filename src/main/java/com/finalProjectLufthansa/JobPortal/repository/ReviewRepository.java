package com.finalProjectLufthansa.JobPortal.repository;

import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.Review;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.resource.ReviewResource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByJobAndEmployer(JobPosting jobPosting,User employer);

    @Query("SELECT r FROM Review r WHERE r.job = :jobPosting AND r.employer = :employer "
            + "AND (:rating IS NULL OR r.rating >= :rating)")
    List<Review> findAllByJobAndEmployerAndRating(
            @Param("jobPosting") JobPosting jobPosting,
            @Param("employer") User employer,
            @Param("rating") Double rating);
}
