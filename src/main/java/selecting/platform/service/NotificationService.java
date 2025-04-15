package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.model.Enum.NotificationType;
import selecting.platform.model.Notification;
import selecting.platform.model.User;
import selecting.platform.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

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
}
