package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import selecting.platform.model.Notification;
import selecting.platform.model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserUserIdAndIsReadFalse(Integer userId);

    List<Notification> user(User user);

    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(@Param("userId") Integer userId);

    Long countByUserUserIdAndIsReadFalse(Integer userUserId);

    List<Notification> findByUserUserId(Integer userId);
}
