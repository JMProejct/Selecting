package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "teacher_available_time")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAvailableTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer availableId;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;    // 월 ~ 일 ENUm

    private LocalTime startTime;
    private LocalTime endTime;
}
