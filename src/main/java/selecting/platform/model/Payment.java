package selecting.platform.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import selecting.platform.model.Enum.PaymentMethod;
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

    private Integer amount;

    private Integer paymentMethod; // TINYINT로 매핑

    private Integer status; // TINYINT로 매핑

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private LocalDateTime paymentDate;
}
