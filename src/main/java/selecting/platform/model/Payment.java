package selecting.platform.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import selecting.platform.model.Enum.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    private Integer amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime paymentDate;

    private String paymentMethod;

    private Status status;
}
