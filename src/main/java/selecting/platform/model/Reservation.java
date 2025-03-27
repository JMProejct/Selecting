package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;
import selecting.platform.model.Enum.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ServicePost post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    private Status status;


}
