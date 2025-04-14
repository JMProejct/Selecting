package selecting.platform.dto.calendar;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeRange {
    private LocalTime start;
    private LocalTime end;

    public boolean overlapsWith(LocalTime slotStart, LocalTime slotEnd) {
        return !(slotEnd.isBefore(start) || slotStart.isAfter(end));
    }
}
