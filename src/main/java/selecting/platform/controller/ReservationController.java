package selecting.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.ReservationService;

@RestController
@RequestMapping("/api")
public class ReservationController {


    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> reserve(
            @RequestBody ReservationRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails ) {
        return ResponseEntity.ok(reservationService.createReservation(requestDto, userDetails.getUser().getUserId()));
    }
}
