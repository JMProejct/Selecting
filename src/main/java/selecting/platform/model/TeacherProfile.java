package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherProfile {

    @Id
    @Column(name = "teacher_id")
    private Integer teacherId;

    @OneToOne
    @MapsId     // teacher_id를 user_id로 사용
    @JoinColumn(name = "teacher_id")
    private User user;

    @Column(length = 255)
    private String education;

    @Column(name = "career_years")
    private Integer careerYears;

    @Column(columnDefinition = "TEXT")
    private String certifications;

    @Column(columnDefinition = "TEXT")
    private String intro;
}
