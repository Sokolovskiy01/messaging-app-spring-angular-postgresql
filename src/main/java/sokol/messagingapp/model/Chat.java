package sokol.messagingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table (name = "chats")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Chat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_user1_id", referencedColumnName = "id", nullable = false)
    private AppUser user1;
    @ManyToOne
    @JoinColumn(name = "app_user2_id", referencedColumnName = "id", nullable = false)
    private AppUser user2;

    private boolean user1Seen;
    private boolean user2Seen;

    /*@OneToMany(mappedBy = "chat")
    private List<Message> messageList;*/ // should be sorted by date and limited to 50. Increment on 30 on scrollUp

    public Chat() {}

    public Chat(AppUser user1, AppUser user2, boolean user1Seen, boolean user2Seen) {
        this.user1 = user1;
        this.user2 = user2;
        this.user1Seen = user1Seen;
        this.user2Seen = user2Seen;
    }

    public Long getId() { return id; }

    public AppUser getUser1() { return user1; }
    public void setUser1(AppUser user1) { this.user1 = user1; }

    public AppUser getUser2() { return user2; }
    public void setUser2(AppUser user2) { this.user2 = user2; }

    /*public List<Message> getMessageList() { return messageList; }
    public void setMessageList(List<Message> messageList) { this.messageList = messageList; }*/

    public boolean isUser1Seen() { return user1Seen; }
    public void setUser1Seen(boolean user1Seen) { this.user1Seen = user1Seen; }

    public boolean isUser2Seen() { return user2Seen; }
    public void setUser2Seen(boolean user2Seen) { this.user2Seen = user2Seen; }

}