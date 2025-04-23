package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import selecting.platform.model.Enum.MessageStatus;

import java.sql.Timestamp;

@Entity
@Table(name = "chatmessage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String messageText;

    @Column
    private Timestamp sentAt;

    @Column(nullable = false)
    @ColumnDefault(MessageStatus.DEFAULT)
    @Enumerated(EnumType.STRING)
    private MessageStatus messageRead;
}
