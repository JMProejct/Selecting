package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.review.ReviewPageResponseDto;
import selecting.platform.dto.review.ReviewResponseDto;
import selecting.platform.dto.review.ReviewWriteRequestDto;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.ReviewService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 사용자 기준 리뷰 조회
    @GetMapping("/review/my")
    public ResponseEntity<ReviewPageResponseDto> getMyReviews(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(reviewService.getMyReviews(user.getUserId()));
    }

    // 특정 게시글 기준 리뷰 조회
    @GetMapping("/post/{postId}/review")
    public ResponseEntity<ReviewPageResponseDto> getPostReviews(
            @PathVariable("postId") Integer postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "latest") String sort) {
        return ResponseEntity.ok(reviewService.getPostReviews(postId, page, sort));
    }

    // 특정 게시글 기준 리뷰 작성
    @PostMapping("/post/{postId}/review")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable("postId") Integer postId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReviewWriteRequestDto dto){
        return ResponseEntity.ok(reviewService.createReview(postId, user.getUserId(),dto));
    }

    // 특정 리뷰 수정
    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<ReviewResponseDto> patchReview(
            @PathVariable("reviewId") Integer reviewId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReviewWriteRequestDto dto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, user.getUserId(),dto));
    }

    // 특정 리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
