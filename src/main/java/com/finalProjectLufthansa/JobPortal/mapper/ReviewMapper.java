package com.finalProjectLufthansa.JobPortal.mapper;

import com.finalProjectLufthansa.JobPortal.model.entity.Review;
import com.finalProjectLufthansa.JobPortal.resource.ReviewResource;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public Review toEntity(ReviewResource reviewResource) {
        Review review = new Review();
        review.setComment(reviewResource.comment());
        review.setRating(reviewResource.rating());
        return review;
    }

    public ReviewResource toReviewResource(Review review) {
        return new ReviewResource(review.getComment(), review.getRating(), review.getEmployer().getName());
    }
}
