package selecting.platform.dto.reservations;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage {

    private String receiverId;
    private String message;
    private String type;
}
