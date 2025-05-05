package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import selecting.platform.model.ChatMessage;
import selecting.platform.model.ChatRoom;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findTopByRoomOrderBySentAtDesc(ChatRoom chatRoom);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.room = :room AND m.sender.userId <> :userId AND m.messageRead = false")
    int countUnreadMessagesByRoomAndNotSender(@Param("room") ChatRoom room, @Param("userId") Integer userId);
}
