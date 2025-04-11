package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import selecting.platform.dto.chatMessage.ChatMessageDto;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
    }
}
