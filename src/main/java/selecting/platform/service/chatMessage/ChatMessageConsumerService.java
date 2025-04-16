package selecting.platform.service.chatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import selecting.platform.dto.chatMessage.ChatMessageDto;
import selecting.platform.model.ChatMessage;
import selecting.platform.model.ChatRoom;
import selecting.platform.model.User;
import selecting.platform.repository.ChatMessageRepository;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageConsumerService {

    private final ChatMessageRepository chatMessageRepository;

    private final Queue<ChatMessageDto> messageQueue = new ConcurrentLinkedQueue<>();

    @KafkaListener(topics = "chatMessage_log",groupId = "chatMessage-group")
    public void listen(ChatMessageDto chatMessageDto) {
        messageQueue.offer(chatMessageDto);
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 4) // 4시간 간격으로 실행
    public void saveMessageToDatabase() {
        if (messageQueue.isEmpty()) {
            log.info("저장할 메시지가 없습니다.");
            return;
        }

        log.info("Kafka 수신 메시지를 DB에 저장 시작");

        List<ChatMessage> messagesToSave = messageQueue.stream()
                .map(dto -> ChatMessage.builder()
                        .room(ChatRoom.builder().roomId(dto.getRoomId()).build())
                        .sender(User.builder().userId(dto.getSenderId()).build())
                        .messageText(dto.getMessageText())
                        .sentAt(dto.getSentAt())
                        .build())
                .collect(Collectors.toList());

        chatMessageRepository.saveAll(messagesToSave);

        messageQueue.clear();

        log.info("총 {}개의 메시지가 저장되었습니다.", messagesToSave.size());
    }
}
