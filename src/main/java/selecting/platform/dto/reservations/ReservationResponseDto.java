package selecting.platform.dto.reservations;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationResponseDto {

    private Integer reservationId;
    private Integer postId;
    private Integer studentId;
    private LocalDateTime reservationDate;
    private String status;
}
