package sokol.messagingapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table (name = "messages")
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="chat_id", referencedColumnName="id", nullable=false)
    private Chat chat_id;
    private Long sender_id;
    private String messageContent;
    private Long replyMessageId; // if user replies to other user message
    private LocalDateTime sentDate;

    public Message() {}

    public Message(Chat chat_id, long sender_id, String messageContent, long replyMessageId, LocalDateTime sentDate) {
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.messageContent = messageContent;
        this.replyMessageId = replyMessageId;
        this.sentDate = sentDate;
    }
}