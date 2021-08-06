package sokol.messagingapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table (name = "chats")
public class Chat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @OneToMany(mappedBy = "chat")
    private List<UsersChats> chatUsers;

    @OneToMany(mappedBy = "chat_id")
    private List<Message> messageList; // should be sorted by date and limited to 50. Increment on 30 on scrollUp

    public Chat() {}

}
