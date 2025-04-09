package selecting.platform.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Enum.Status;
import selecting.platform.model.Reservation;
import selecting.platform.model.ServicePost;
import selecting.platform.model.User;
import selecting.platform.repository.ReservationRepository;
import selecting.platform.repository.ServicePostRepository;
import selecting.platform.repository.UserRepository;

@Service
public class ReservationService {

    private final ServicePostRepository servicePostRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ServicePostRepository servicePostRepository, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.servicePostRepository = servicePostRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    
    // 예약하기
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, Integer studentId) {
        ServicePost post = servicePostRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Reservation reservation = Reservation.builder()
                .post(post)
                .student(userRepository.findById(studentId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .reservationDate(requestDto.getReservationDate())
                .status(Status.CANCELLED) // 임의 값 추가
                .build();

        reservationRepository.save(reservation);

        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .postId(post.getPostId())
                .studentId(studentId)
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

        reservation.setStatus(Status.CONFIRMED);
        reservationRepository.save(reservation);

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

        reservation.setStatus(Status.CANCELLED);
        reservationRepository.save(reservation);

        return ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .postId(reservation.getPost().getPostId())
                .studentId(reservation.getStudent().getUserId())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().name())
                .build();
    }
}
