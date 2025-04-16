package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.dto.reservations.NotificationResponseDto;
import selecting.platform.model.Notification;
import selecting.platform.model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserUserIdAndIsReadFalse(Integer userId);

    List<Notification> user(User user);

    List<NotificationResponseDto> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
}
