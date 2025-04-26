package selecting.platform.dto.review;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewListResponseDto {
    private int reviewCount;
    private List<ReviewResponseDto> reviews;
}
