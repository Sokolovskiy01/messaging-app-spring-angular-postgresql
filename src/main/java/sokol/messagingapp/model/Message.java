package sokol.messagingapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table (name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat", referencedColumnName="id", nullable=false)
    private Chat chat;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private AppUser sender;

    @Column(length = 2048, nullable = false)
    private String messageContent;
    private LocalDateTime sentDate;

    public Message() {}

    public Message(Chat chat, AppUser sender, String messageContent, LocalDateTime sentDate) {
        this.chat = chat;
        this.sender = sender;
        this.messageContent = messageContent;
        this.sentDate = sentDate;
    }

    public Long getId() { return id; }

    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public AppUser getSender() { return sender; }
    public void setSender(AppUser sender_id) { this.sender = sender_id; }

    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }

}