package selecting.platform.dto.reservations;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationRequestDto {
    private Integer postId;
    private LocalDateTime reservationDate;
}
