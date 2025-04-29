package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.dto.chatRoom.ChatRoomDto;
import selecting.platform.dto.chatRoom.ChatRoomResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.ChatMessage;
import selecting.platform.model.ChatRoom;
import selecting.platform.model.ServicePost;
import selecting.platform.model.User;
import selecting.platform.repository.ChatMessageRepository;
import selecting.platform.repository.ChatRoomRepository;
import selecting.platform.repository.ServicePostRepository;
import selecting.platform.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ServicePostRepository servicePostRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    public void createChatRoom(ChatRoomDto chatRoomDto,Integer userId) {

        chatRoomDto.setUserId(userId);

        ServicePost post = servicePostRepository.findById(chatRoomDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(chatRoomDto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .user(user)
                .createdAt(chatRoomDto.getCreatedAt())
                .build();

        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 리스트 형식으로 조회
    public List<ChatRoomResponseDto> getChatRooms(Integer userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserUserId(userId);

        return chatRooms.stream()
                .filter(chatRoom -> {
                    boolean isPostOwner = chatRoom.getPost().getUser().getUserId().equals(userId);
                    return isPostOwner
                            ? chatRoom.isPostUserExitStatus()
                            : chatRoom.isUserExitStatus();
                })
                .map(chatRoom -> {
            // 상대방 가져오기
            User chatPartner = chatRoom.getUser().getUserId().equals(userId)
                    ? chatRoom.getPost().getUser()
                    : chatRoom.getUser();

            // 마지막 메시지 가져오기
            ChatMessage lastMessage = chatMessageRepository
                    .findTopByRoomOrderBySentAtDesc(chatRoom)
                    .orElse(null);

            int unreadCount = chatMessageRepository
                    .countUnreadMessagesByRoomAndNotSender(chatRoom, userId);

            return ChatRoomResponseDto.builder()
                    .roomId(chatRoom.getRoomId())
                    .chatPartnerId(chatPartner.getUserId())
                    .chatPartnerName(chatPartner.getName())
                    .chatPartnerProfileImage(chatPartner.getProfileImage())
                    .lastMessage(lastMessage != null ? lastMessage.getMessageText() : "")
                    .lastMessageTime(lastMessage != null ? lastMessage.getSentAt().toString() : null)
                    .unreadMessageCount(unreadCount)
                    .build();
        }).toList();
    }

    public void exitChatRoom(Integer roomId, Integer userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUNT));

        if (chatRoom.getPost().getUser().getUserId().equals(userId)) {
            chatRoom.setPostUserExitStatus(false);
        } else if (chatRoom.getUser().getUserId().equals(userId)) {
            chatRoom.setUserExitStatus(false);
        } else {
            throw new CustomException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        chatRoomRepository.save(chatRoom);
    }
}
