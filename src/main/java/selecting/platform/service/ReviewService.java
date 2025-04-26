package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.dto.review.ReviewListResponseDto;
import selecting.platform.dto.review.ReviewResponseDto;
import selecting.platform.model.Review;
import selecting.platform.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewListResponseDto getMyReviews(Integer userId) {
        List<Review> reviews = reviewRepository.findByUserUserId(userId);

        List<ReviewResponseDto> responseDto = reviews.stream()
                .map(ReviewResponseDto::from)
                .toList();

        return ReviewListResponseDto.builder()
                .reviews(responseDto)
                .reviewCount(responseDto.size())
                .build();
    }

    public ReviewListResponseDto getPostReviews(Integer postId) {
        List<Review> reviews = reviewRepository.findByPostPostId(postId);

        List<ReviewResponseDto> responseDto = reviews.stream()
                .map(ReviewResponseDto::from)
                .toList();

        return ReviewListResponseDto.builder()
                .reviews(responseDto)
                .reviewCount(responseDto.size())
                .build();
    }
}
