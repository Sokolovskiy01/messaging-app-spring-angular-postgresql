package sokol.messagingapp.model;

import org.hibernate.annotations.Check;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private AppUser sender_id;

    @Column(length = 2048, nullable = false)
    private String messageContent;
    private LocalDateTime sentDate;
        this.sentDate = sentDate;
    }

    public Long getId() { return id; }

    public Chat getChat_id() { return chat_id; }
    public void setChat_id(Chat chat_id) { this.chat_id = chat_id; }

    public AppUser getSender_id() { return sender_id; }
    public void setSender_id(AppUser sender_id) { this.sender_id = sender_id; }

    public Message() {}

    public Message(Chat chat_id, AppUser sender_id, String messageContent, LocalDateTime sentDate) {
        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.messageContent = messageContent;

    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }

}