package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import selecting.platform.dto.reservations.NotificationMessage;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NotificationSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/notify")  // /pub/notify 로 요청
    public void sendNotification(NotificationMessage message) {
        log.info("\uD83D\uDD14 알림 전송 대상: {}, 내용: {}", message.getReceiverId(), message.getMessage());

        // 실시간 전송
        simpMessagingTemplate.convertAndSend("/sub/notifications/" + message.getReceiverId(), message);
    }
}
