package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.review.ReviewListResponseDto;
import selecting.platform.dto.review.ReviewResponseDto;
import selecting.platform.dto.review.ReviewWriteRequestDto;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.ReviewService;

@RestController
@RequestMapping("/api/review/")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/my")
    public ResponseEntity<ReviewListResponseDto> getMyReviews(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(reviewService.getMyReviews(user.getUserId()));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ReviewListResponseDto> getPostReviews(
            @PathVariable Integer postId) {
        return ResponseEntity.ok(reviewService.getPostReviews(postId));
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Integer postId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReviewWriteRequestDto dto){
        return ResponseEntity.ok(reviewService.createReview(postId, user.getUserId(),dto));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> patchReview(
            @PathVariable Integer reviewId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReviewWriteRequestDto dto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, user.getUserId(),dto));
    }
}
