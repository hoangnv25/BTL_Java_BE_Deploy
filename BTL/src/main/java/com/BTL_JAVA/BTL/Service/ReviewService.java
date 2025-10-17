package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.ReviewRequest;
import com.BTL_JAVA.BTL.DTO.Response.ReviewResponse;
import com.BTL_JAVA.BTL.Entity.Review;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.ReviewRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReviewService {
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;


    // tạo mới review
    public ReviewResponse createReview(ReviewRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Nếu có rating thì cập nhật
        if (request.getRating() != null) {
            user.setRating(request.getRating());
        }

        Review review = Review.builder()
                .user(user)
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    // Cập nhật review
    public ReviewResponse updateReview(Integer reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!review.getUser().getFullName().equals(username)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        review.setComment(request.getComment());
        if (request.getRating() != null) {
            review.getUser().setRating(request.getRating());
        }

        Review updatedReview = reviewRepository.save(review);
        return mapToResponse(updatedReview);
    }

    // Lấy tất cả review
    public List<ReviewResponse> getAllReview() {
        return reviewRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Xóa review
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));

        if (!isAdmin && review.getUser().getId() != currentUser.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        reviewRepository.delete(review);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .fullName(review.getUser().getFullName())
                .rating(review.getUser().getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
