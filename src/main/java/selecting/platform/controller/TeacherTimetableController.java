package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.reservations.AvailableTimeRequestDto;
import selecting.platform.dto.reservations.AvailableTimeResponseDto;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/teacher/timetable")
@RequiredArgsConstructor
public class TeacherTimetableController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<AvailableTimeResponseDto>> getTimetable(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(service.getAvailableTimes(userDetails.getUser()));
    }

    @PostMapping
    public ResponseEntity<Void> addAvailableTime(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AvailableTimeRequestDto dto) {

        service.addAvailableTime(userDetails.getUser(), dto);
        return ResponseEntity.ok().build();
    }
}
