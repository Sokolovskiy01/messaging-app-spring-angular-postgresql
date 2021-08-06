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
    private long id;
    @ManyToOne
    @JoinColumn(name="chat_id", referencedColumnName="id", nullable=false)
    private Chat chat_id;
    private long sender_id;
    private String messageContent;
    private long replyMessageId; // if user replies to other user message
    private LocalDateTime sentDate;

    public Message() {}

}
