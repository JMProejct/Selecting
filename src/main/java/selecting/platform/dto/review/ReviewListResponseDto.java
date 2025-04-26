package selecting.platform.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReviewListResponseDto {
    private int reviewCount;
    private List<ReviewResponseDto> reviews;
}
