package selecting.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.reservations.ReservationRequestDto;
import selecting.platform.dto.reservations.ReservationResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.ReservationService;
import selecting.platform.util.AuthUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthUtil authUtil;

    public ReservationController(ReservationService reservationService, AuthUtil authUtil) {
        this.reservationService = reservationService;
        this.authUtil = authUtil;
    }

    // 예약
    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> reserve(
            @RequestBody ReservationRequestDto requestDto,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(reservationService.createReservation(requestDto, authUtil.getUserFromToken(token).getUserId()));
    }

    // 예약 승인
    @PatchMapping("/reservations/{id}/accept")
    public ResponseEntity<ReservationResponseDto> accept(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reservationService.approveReservation(id, userDetails.getUser()));
    }

    // 예약 거절
    @PatchMapping("/reservations/{id}/reject")
    public ResponseEntity<ReservationResponseDto> reject(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reservationService.rejectReservation(id, userDetails.getUser()));
    }

    // 나의 예약 조회
    @GetMapping("/reservations/my")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();

        if (user.getRole() == Role.TUTOR) {
            return ResponseEntity.ok(reservationService.getReservationsByTeacher(user));
        } else if (user.getRole() == Role.NORMAL) {
            return ResponseEntity.ok(reservationService.getReservationsByStudent(user));
        } else {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }
    }


}
