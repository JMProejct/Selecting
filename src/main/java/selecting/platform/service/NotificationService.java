package selecting.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.dto.reservations.NotificationResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
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
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
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


    // 알림 읽음 처리
    @Transactional
    public void markAsRead(Integer notificationId, Integer userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    // 안 읽은 알림 카운트
    public Long countunreadNotifications(Integer userId) {
        return notificationRepository.countByUserUserIdAndIsReadFalse(userId);
    }


    // 전체 읽음 처리 메서드
    @Transactional
    public void markAllAsRead(Integer userId) {
        List<Notification> unreadList = notificationRepository.findByUserUserIdAndIsReadFalse(userId);

        for (Notification notification : unreadList) {
            notification.setIsRead(true);
        }

        notificationRepository.saveAll(unreadList); // 성능상 일괄 저장
    }


    // 알림 단건 삭제
    @Transactional
    public void deleteNotification(Integer notificationId, Integer userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        notificationRepository.delete(notification);
    }

    // 알림 전체 삭제
    @Transactional
    public void deleteAllNotifications(Integer userId) {
        List<Notification> userNotifications = notificationRepository.findByUserUserId(userId);
        notificationRepository.deleteAll(userNotifications);
    }
}
