package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.ReviewRequest;
import com.BTL_JAVA.BTL.DTO.Response.ReviewResponse;
import com.BTL_JAVA.BTL.Service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j

public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ApiResponse<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(request))
                .build();
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<ReviewResponse> updateReview(@PathVariable("reviewId") Integer reviewId, @RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReview(reviewId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ReviewResponse>> getAllReviews() {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getAllReview())
                .build();
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<Void> deleteReview(@PathVariable("reviewId") Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.<Void>builder()
                .message("Đã xóa thành công !")
                .build();
    }
}
