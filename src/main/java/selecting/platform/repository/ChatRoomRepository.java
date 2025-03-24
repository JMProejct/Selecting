package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
