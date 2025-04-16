package selecting.platform.service.chatMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import selecting.platform.dto.chatMessage.ChatMessageDto;
import selecting.platform.repository.ChatRoomRepository;
import selecting.platform.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ChatMessageProducerService {
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;

    private final String TOPIC = "chatMessage_log";

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void send(ChatMessageDto message) {
        validate(message);
        kafkaTemplate.send(TOPIC, message);
    }

    public void validate(ChatMessageDto message) {
        // 검증: 채팅방 존재 여부
        if (!chatRoomRepository.existsById(message.getRoomId())) {
            throw new IllegalArgumentException("채팅방이 존재하지 않습니다.");
        }

        // 검증: 사용자 존재 여부
        if (!userRepository.existsById(message.getSenderId())) {
            throw new IllegalArgumentException("발신 유저가 존재하지 않습니다.");
        }
    }
}
