package selecting.platform.dto.review;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewPageResponseDto {
    private int reviewCount;
    private boolean hasNext;
    private List<ReviewResponseDto> reviews;
}
