package selecting.platform.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicePostDetailDto {
    private Integer postId;
    private String title;
    private String description;
    private String location;
    private BigDecimal price;
    private String subcategoryName;

    private String teacherName;
    private String profileImage;
    private String education;
    private Integer careerYears;
    private String certifications;
    private String intro;
}
