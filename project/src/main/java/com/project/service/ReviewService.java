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

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Optional<Review> updateReview(
            Long id,
            Review reviewDetails) {

        return reviewRepository.findById(id)
                .map(existingReview -> {

                    existingReview.setRating(
                            reviewDetails.getRating()
                    );

                    existingReview.setComment(
                            reviewDetails.getComment()
                    );

                    return reviewRepository.save(existingReview);
                });
    }

    public boolean deleteReview(Long id) {

        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }

        return false;
    }
}