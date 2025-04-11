package selecting.platform.dto.reservations;

import lombok.Data;

import java.time.LocalTime;

@Data
public class AvailableTimeRequestDto {
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
