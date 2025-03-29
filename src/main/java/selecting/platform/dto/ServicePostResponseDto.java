package selecting.platform.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePostResponseDto {

    private Integer postId;
    private String title;
    private String location;
    private String subcategoryName;
    private String teacherName;
    private BigDecimal price;
}
