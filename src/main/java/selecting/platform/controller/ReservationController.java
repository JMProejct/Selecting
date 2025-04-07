package selecting.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.service.ReservationService;
import selecting.platform.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthUtil authUtil;

    public ReservationController(ReservationService reservationService, AuthUtil authUtil) {
        this.reservationService = reservationService;
        this.authUtil = authUtil;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> reserve(
            @RequestBody ReservationRequestDto requestDto,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(reservationService.createReservation(requestDto, authUtil.getUserFromToken(token).getUserId()));
    }
}
