package selecting.platform.dto.chatMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private Integer roomId;
    private Integer senderId;
    private Integer receiverId;
    private String message;
}
