package selecting.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.security.CustomUserDetails;
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

    @PatchMapping("/reservations/{id}/accept")
    public ResponseEntity<ReservationResponseDto> accept(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reservationService.approveReservation(id, userDetails.getUser()));
    }

    @PatchMapping("/reservations/{id}/reject")
    public ResponseEntity<ReservationResponseDto> reject(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reservationService.rejectReservation(id, userDetails.getUser()));
    }
}
