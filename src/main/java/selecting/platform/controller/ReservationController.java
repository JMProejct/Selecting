package selecting.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.model.User;
import selecting.platform.service.ReservationService;
import selecting.platform.service.UserService;
import selecting.platform.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final AuthUtil authUtil;

    private final ReservationService reservationService;

    public ReservationController(UserService userService, AuthUtil authUtil, ReservationService reservationService) {
        this.authUtil = authUtil;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> reserve(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ReservationRequestDto requestDto) {

        String token = authHeader.replace("Bearer ", "");
        User user = authUtil.getUserFromToken(token);
        return ResponseEntity.ok(reservationService.createReservation(requestDto, user.getUserId()));
    }
}
