package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.dto.reservations.NotificationResponseDto;
import selecting.platform.model.Enum.NotificationType;
import selecting.platform.model.Notification;
import selecting.platform.model.User;
import selecting.platform.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 전송
    public void send(User toUser, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .user(toUser)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
    
    
    // 알림 목록 조회
    public List<NotificationResponseDto> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notification -> NotificationResponseDto.builder()
                        .id(notification.getId())
                        .message(notification.getMessage())
                        .type(notification.getType())
                        .isRead(notification.getIsRead())
                        .createdAt(notification.getCreatedAt())
                        .build())
                .toList();
    }
}
