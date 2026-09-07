package com.project.service;

import com.project.entity.Review;
import com.project.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;


    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public Review createReview(Review review){
        return reviewRepository.save(review);

    }

   public List<Review> getAllReview(){
        return reviewRepository.findAll();
   }
   public Optional<Review> getReviewById(Long id){
        return reviewRepository.findById(id);
   }
   public Review updateReview(Long id,Review review){
        Review existingReview = reviewRepository.findById(id).orElseThrow(()->new RuntimeException("Review not found"));
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        return reviewRepository.save(existingReview);

    }
    public void deleteReview(Long id){
        reviewRepository.deleteById(id );
    }
}
