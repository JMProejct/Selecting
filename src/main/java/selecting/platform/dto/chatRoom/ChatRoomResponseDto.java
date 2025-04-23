package selecting.platform.dto.chatRoom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private Integer roomId;
    private Integer chatPartnerId;
    private String chatPartnerName;
    private String chatPartnerProfileImage;
    private String lastMessage;               // 마지막 메시지 내용
    private String lastMessageTime;           // 마지막 메시지 전송 시간 (포맷팅된 문자열)
    private int unreadMessageCount;           // 내가 아직 읽지 않은 메시지 수
}
