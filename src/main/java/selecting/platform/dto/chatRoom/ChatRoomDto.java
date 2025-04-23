package selecting.platform.dto.chatRoom;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatRoomDto {
    private Integer roomId;
    private Integer postId;
    private Integer userId;
    private Timestamp createdAt;
}
