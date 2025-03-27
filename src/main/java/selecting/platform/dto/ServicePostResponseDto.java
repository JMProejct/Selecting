package selecting.platform.dto;


import lombok.*;
import selecting.platform.model.Enum.SubCategoryKind;

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
    private SubCategoryKind subcategoryKind;
    private String teacherName;
    private BigDecimal price;
}
