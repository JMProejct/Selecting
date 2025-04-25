package selecting.platform.dto.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherCardResponseDto {
    private Integer teacherId;
    private String name;
    private String profileImage;
    private List<String> categories;
    private String intro;
    private Integer careerYears;
    private double rating;       // 추후 구현용
    private int reviewCount;     // 추후 구현용


    public TeacherCardResponseDto(Integer teacherId, String name, String profileImage, String intro, Integer careerYears) {
        this.teacherId = teacherId;
        this.name = name;
        this.profileImage = profileImage;
        this.intro = intro;
        this.careerYears = careerYears;
    }


}
