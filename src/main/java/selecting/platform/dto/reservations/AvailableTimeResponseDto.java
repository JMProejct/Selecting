package selecting.platform.dto.reservations;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class AvailableTimeResponseDto {

    private Integer availableId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
