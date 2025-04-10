package selecting.platform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Enum.Status;
import selecting.platform.model.Reservation;
import selecting.platform.model.ReservationLog;
import selecting.platform.model.ServicePost;
import selecting.platform.model.User;
import selecting.platform.repository.ReservationLogRepository;
import selecting.platform.repository.ReservationRepository;
import selecting.platform.repository.ServicePostRepository;
import selecting.platform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ServicePostRepository servicePostRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReservationLogRepository reservationLogRepository;

    public ReservationService(ServicePostRepository servicePostRepository, UserRepository userRepository, ReservationRepository reservationRepository, ReservationLogRepository reservationLogRepository) {
        this.servicePostRepository = servicePostRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationLogRepository = reservationLogRepository;
    }

    
    // 예약하기
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, Integer studentId) {
        ServicePost post = servicePostRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User student = userRepository.findById(studentId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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


    public List<ReservationResponseDto> getReservationsByTeacher(User teacher) {
        List<Reservation> reservations = reservationRepository.findByPost_User(teacher);
        return reservations.stream()
                .map(res -> ReservationResponseDto.builder()
                        .reservationId(res.getReservationId())
                        .postId(res.getPost().getPostId())
                        .postTitle(res.getPost().getTitle())
                        .studentId(res.getStudent().getUserId())
                        .studentName(res.getStudent().getName())
                        .reservationDate(res.getReservationDate())
                        .status(res.getStatus().name())
                        .build())
                .toList();
    }

    public List<ReservationResponseDto> getReservationsByStudent(User student) {
        List<Reservation> reservations = reservationRepository.findByStudent(student);
        return reservations.stream()
                .map(res -> ReservationResponseDto.builder()
                        .reservationId(res.getReservationId())
                        .postId(res.getPost().getPostId())
                        .postTitle(res.getPost().getTitle())
                        .studentId(res.getStudent().getUserId())
                        .studentName(res.getStudent().getName())
                        .reservationDate(res.getReservationDate())
                        .status(res.getStatus().name())
                        .build())
                .toList();
    }
}
