package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import selecting.platform.model.Enum.ExitStatus;

import java.sql.Timestamp;

@Entity
@Table(name = "chatroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ServicePost post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(ExitStatus.DEFAULT)
    private ExitStatus userExitStatus;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(ExitStatus.DEFAULT)
    private ExitStatus postUserExitStatus;

    @PrePersist
    protected void prePersist() {
        if(createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }
}
