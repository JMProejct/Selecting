package selecting.platform.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import selecting.platform.model.Review;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ReviewResponseDto {
    private Integer reviewId;
    private String profileImage;
    private String name;
    private Integer rating;
    private String comment;
    private Timestamp created;

    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .profileImage(review.getUser().getProfileImage())
                .name(review.getUser().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .created(review.getCreatedAt())
                .build();
    }
}
