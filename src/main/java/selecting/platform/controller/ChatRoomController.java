package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.chatRoom.ChatRoomDto;
import selecting.platform.dto.chatRoom.ChatRoomResponseDto;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.ChatRoomService;

import java.util.List;

@RestController
@RequestMapping("/api/chatRoom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    public ResponseEntity<Integer> createChatRoom(@RequestBody ChatRoomDto chatRoomDto,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.createChatRoom(chatRoomDto, userDetails.getUserId());
        return ResponseEntity.ok(chatRoomDto.getRoomId());
    }

    @GetMapping("/my")
    public ResponseEntity<List<ChatRoomResponseDto>> getMyChatRooms
            (@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(chatRoomService.getChatRooms(userDetails.getUserId()));
    }

    @DeleteMapping("/exit/{roomId}")
    public ResponseEntity<?> exitChatRoom(@PathVariable Integer roomId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.exitChatRoom(roomId, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }
}