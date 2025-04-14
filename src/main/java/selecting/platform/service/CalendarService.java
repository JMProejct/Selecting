package selecting.platform.service;


import selecting.platform.dto.calendar.TimeSlotDto;

import java.time.LocalDate;
import java.util.List;

public interface CalendarService {
    List<TimeSlotDto> getAvailableSlots(Integer teacherId, LocalDate date);
}
