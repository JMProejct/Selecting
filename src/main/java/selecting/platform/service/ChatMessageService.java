package selecting.platform.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import selecting.platform.dto.chatMessage.ChatMessageDto;

@Service
public class ChatMessageService {
    RedisTemplate<String, ChatMessageDto> redisTemplate;

    public void saveMessageToRedis(ChatMessageDto chatMessageDto) {
        String roomKey = "chat:room:" + chatMessageDto.getRoomId() + ":message";
        String timeKey = "chat:room" + chatMessageDto.getRoomId() + ":lastTime";

    }
}
