package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.reservations.NotificationResponseDto;
import selecting.platform.model.User;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 로그인한 사용자의 알림 목록 조회
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(notificationService.getUserNotifications(user.getUserId()));
    }

    // 알림 읽음 처리
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        notificationService.markAsRead(id, user.getUserId());
        return ResponseEntity.ok().build();
    }

    // 안 읽은 알림 카운트
    @GetMapping("/unread/count")
    public ResponseEntity<Long> countUnread(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(notificationService.countunreadNotifications(user.getUserId()));
    }


    // 알림 전체 읽음
    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        notificationService.markAllAsRead(user.getUserId());
        return ResponseEntity.ok().build();
    }


    // 알림 단건 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.deleteNotification(id, userDetails.getUser().getUserId());
        return ResponseEntity.ok().build();
    }


    // 알림 전체 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteAllNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.deleteAllNotifications(userDetails.getUser().getUserId());
        return ResponseEntity.ok().build();
    }
}
