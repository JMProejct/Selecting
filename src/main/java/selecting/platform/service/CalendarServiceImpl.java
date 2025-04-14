package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.dto.calendar.TimeSlotDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Reservation;
import selecting.platform.model.TeacherAvailableTime;
import selecting.platform.model.User;
import selecting.platform.repository.ReservationRepository;
import selecting.platform.repository.TeacherAvailableTimeRepository;
import selecting.platform.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final UserRepository userRepository;
    private final TeacherAvailableTimeRepository teacherAvailableTimeRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<TimeSlotDto> getAvailableSlots(Integer teacherId, LocalDate date) {

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        DayOfWeek dayOfWeek = date.getDayOfWeek();


        // 교사 가능한 시간대 조회
        List<TeacherAvailableTime> availableTimes = teacherAvailableTimeRepository
                .findByTeacherAndDayOfWeek(teacher, dayOfWeek);

        // 해당 날짜의 예약 리스트 조회
        List<Reservation> reservations = reservationRepository
                .findByPost_UserAndReservationDateBetween(
                        teacher,
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay());


        // 예약된 시간 리스트로 정리
        Set<LocalTime> reservedTimes = reservations.stream()
                .map(r -> r.getReservationDate().toLocalTime())
                .collect(Collectors.toSet());

        // 반환할 시간대 리스트 구성
        List<TimeSlotDto> result = new ArrayList<>();

        for (TeacherAvailableTime availableTime : availableTimes) {
            LocalTime start = availableTime.getStartTime();
            LocalTime end = availableTime.getEndTime();

            while (start.isBefore(end)) {
                LocalTime next = start.plusMinutes(60); // 1시간
                String status = reservedTimes.contains(start) ? "RESERVED" : "AVAILABLE";

                result.add(new TimeSlotDto(start, next, status));
                start = next;
            }
        }

        return result;
    }
}
