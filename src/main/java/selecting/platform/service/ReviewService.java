package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import selecting.platform.dto.review.ReviewPageResponseDto;
import selecting.platform.dto.review.ReviewResponseDto;
import selecting.platform.dto.review.ReviewWriteRequestDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Review;
import selecting.platform.model.ServicePost;
import selecting.platform.model.User;
import selecting.platform.repository.ReviewRepository;
import selecting.platform.repository.ServicePostRepository;
import selecting.platform.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ServicePostRepository servicePostRepository;

    public ReviewPageResponseDto getMyReviews(Integer userId) {
        List<Review> reviews = reviewRepository.findByUserUserId(userId);

        List<ReviewResponseDto> responseDto = reviews.stream()
                .map(ReviewResponseDto::from)
                .toList();

        return ReviewPageResponseDto.builder()
                .reviews(responseDto)
                .reviewCount(responseDto.size())
                .build();
    }

    public ReviewPageResponseDto getPostReviews(Integer postId, int page, String sort) {
        Sort sorting = switch (sort) {
            case "ratingHigh" -> Sort.by(Sort.Direction.DESC, "rating");
            case "ratingLow" -> Sort.by(Sort.Direction.ASC, "rating");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        Pageable pageable = PageRequest.of(page, 5, sorting);
        Page<Review> reviews = reviewRepository.findByPostPostId(postId, pageable);

        Page<ReviewResponseDto> responseDto = reviews.map(ReviewResponseDto::from);

        return ReviewPageResponseDto.builder()
                .reviews(responseDto.getContent())
                .reviewCount(responseDto.getNumberOfElements())
                .hasNext(responseDto.hasNext())
                .build();
    }

    public ReviewResponseDto updateReview(Integer reviewId, Integer userId, ReviewWriteRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if(!(review.getUser().getUserId().equals(userId))) {
            throw new CustomException(ErrorCode.AUTH_FORBIDDEN);
        }

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);

        return ReviewResponseDto.from(review);
    }

    public ReviewResponseDto createReview(Integer postId, Integer userId, ReviewWriteRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        ServicePost post = servicePostRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        Review review = Review.builder()
                .user(user)
                .post(post)
                .comment(dto.getComment())
                .rating(dto.getRating())
                .build();

        reviewRepository.save(review);

        return ReviewResponseDto.from(review);
    }

    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
