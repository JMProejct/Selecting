package selecting.platform.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeSlotDto {

    private LocalTime startTime;
    private LocalTime endTime;
    private String status;  // "AVAILABLE", "RESERVED"
}
