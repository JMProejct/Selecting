package selecting.platform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import selecting.platform.dto.reservations.AvailableTimeRequestDto;
import selecting.platform.dto.reservations.AvailableTimeResponseDto;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.*;
import selecting.platform.model.Enum.Status;
import selecting.platform.repository.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ServicePostRepository servicePostRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReservationLogRepository reservationLogRepository;
    private final TeacherAvailableTimeRepository teacherAvailableTimeRepository;

    public ReservationService(ServicePostRepository servicePostRepository, UserRepository userRepository, ReservationRepository reservationRepository, ReservationLogRepository reservationLogRepository, TeacherAvailableTimeRepository teacherAvailableTimeRepository) {
        this.servicePostRepository = servicePostRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationLogRepository = reservationLogRepository;
        this.teacherAvailableTimeRepository = teacherAvailableTimeRepository;
    }

    
    // 예약하기
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, Integer studentId) {
        ServicePost post = servicePostRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User student = userRepository.findById(studentId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!isReservable(post.getUser(), requestDto.getReservationDate())) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_AVAILABLE);
        }

        Reservation reservation = Reservation.builder()
                .post(post)
                .student(student)
                .reservationDate(requestDto.getReservationDate())
                .status(Status.PENDING)
                .build();

        reservationRepository.save(reservation);

        // 로그 작성
        reservationLogRepository.save(ReservationLog.builder()
                .reservation(reservation)
                .changedBy(student.getUsername())
                .previousStatus(null)
                .newStatus(Status.PENDING.name())
                .changedAt(LocalDateTime.now())
                .build());

        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .postId(post.getPostId())
                .postTitle(post.getTitle())
                .studentId(student.getUserId())
                .studentName(student.getName())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }


    // 예약 승인
    public ReservationResponseDto approveReservation(Integer reservationId, User teacher) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.getPost().getUser().getUserId().equals(teacher.getUserId())) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        Status oldStatus = reservation.getStatus();

        validateStatusTransition(oldStatus, reservation.getStatus());

        reservation.setStatus(Status.CONFIRMED);
        reservationRepository.save(reservation);


        // 로그 남기기
        ReservationLog log = ReservationLog.builder()
                .reservation(reservation)
                .changedBy(teacher.getUsername())
                .previousStatus(oldStatus.name())
                .newStatus(reservation.getStatus().name())
                .changedAt(LocalDateTime.now())
                .build();
        reservationLogRepository.save(log);


        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .postId(reservation.getPost().getPostId())
                .studentId(reservation.getStudent().getUserId())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }



    // 예약 거절
    public ReservationResponseDto rejectReservation(Integer reservationId, User teacher) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.getPost().getUser().getUserId().equals(teacher.getUserId())) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        Status oldStatus = reservation.getStatus();
        Status newStatus = Status.CANCELLED;

        validateStatusTransition(oldStatus, newStatus);

        reservation.setStatus(newStatus);
        reservationRepository.save(reservation);


        reservationLogRepository.save(ReservationLog.builder()
                .reservation(reservation)
                .changedBy(teacher.getUsername())
                .previousStatus(oldStatus.name())
                .newStatus(newStatus.name())
                .changedAt(LocalDateTime.now())
                .build());

        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .postId(reservation.getPost().getPostId())
                .postTitle(reservation.getPost().getTitle())
                .studentId(reservation.getStudent().getUserId())
                .studentName(reservation.getStudent().getName())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }


    private void validateStatusTransition(Status current, Status next) {
        if (current == Status.CONFIRMED && next == Status.PENDING) {
            throw new CustomException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
    }


    // 학생 기준 나의 예약 조회
    public List<ReservationResponseDto> getReservationsByStudent(User student) {
        return reservationRepository.findByStudent(student).stream()
                .map(this::toDto)
                .toList();
    }


    // 선생 기준 나의 예약 조회
    public List<ReservationResponseDto> getReservationsByTeacher(User teacher) {
        return reservationRepository.findByTeacher(teacher).stream()
                .map(this::toDto)
                .toList();
    }


    private ReservationResponseDto toDto(Reservation reservation) {
        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .postId(reservation.getPost().getPostId())
                .postTitle(reservation.getPost().getTitle())
                .studentId(reservation.getStudent().getUserId())
                .studentName(reservation.getStudent().getName())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }


    // 교사 시간 확인
    public List<AvailableTimeResponseDto> getAvailableTimes(User teacher) {
        return teacherAvailableTimeRepository.findByTeacher(teacher).stream()
                .map(t -> AvailableTimeResponseDto.builder()
                        .availableId(t.getAvailableId())
                        .dayOfWeek(t.getDayOfWeek().name())
                        .startTime(t.getStartTime())
                        .endTime(t.getEndTime())
                        .build())
                .toList();
    }

    // 교사 시간 확정
    @Transactional
    public void addAvailableTime(User teacher, AvailableTimeRequestDto dto) {
        TeacherAvailableTime time = TeacherAvailableTime.builder()
                .teacher(teacher)
                .dayOfWeek(DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase()))
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();

        teacherAvailableTimeRepository.save(time);
    }

    // 예약 가능한지 자동 확인
    public boolean isReservable(User teacher, LocalDateTime requestedDateTime) {
        DayOfWeek requestedDay = requestedDateTime.getDayOfWeek();
        LocalTime requestedTime = requestedDateTime.toLocalTime();


        // 가능한 시간대 확인
        boolean inAvailableTime = teacherAvailableTimeRepository.existsByTeacherAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThan(
                teacher,
                requestedDay,
                requestedTime,
                requestedTime
        );

        if (inAvailableTime) {return false;}

        // 중복 예약 확인
        boolean hasConflict = reservationRepository.existsByPost_UserAndReservationDate(teacher, requestedDateTime);

        return !hasConflict;
    }
}
