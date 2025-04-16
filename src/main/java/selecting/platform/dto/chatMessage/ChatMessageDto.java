package selecting.platform.dto.chatMessage;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatMessageDto {
    private Integer roomId;
    private Integer senderId;
    private Integer receiverId;
    private String messageText;
    private Timestamp sentAt = new Timestamp(System.currentTimeMillis());
}
