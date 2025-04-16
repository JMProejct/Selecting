package selecting.platform.dto.reservations;

import lombok.Builder;
import lombok.Data;
import selecting.platform.model.Enum.NotificationType;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {

    private Integer id;
    private String message;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
