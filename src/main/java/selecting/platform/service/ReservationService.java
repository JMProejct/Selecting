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

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, Integer studentId) {
        ServicePost post = servicePostRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Reservation reservation = Reservation.builder()
                .post(post)
                .student(userRepository.findById(studentId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .reservationDate(requestDto.getReservationDate())
                .status(Status.PENDING) // 임의로 추가
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
}
