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
    private Long id;

    @OneToMany(mappedBy = "chat")
    private List<UsersChats> chatUsers;

    @OneToMany(mappedBy = "chat_id")
    private List<Message> messageList; // should be sorted by date and limited to 50. Increment on 30 on scrollUp

    public Chat() {}

    public Chat(List<UsersChats> chatUsers, List<Message> messageList) {
        this.chatUsers = chatUsers;
        this.messageList = messageList;
    }

    public Long getId() { return id; }

    public List<UsersChats> getChatUsers() { return chatUsers; }
    public void setChatUsers(List<UsersChats> chatUsers) { this.chatUsers = chatUsers; }

    public List<Message> getMessageList() { return messageList; }
    public void setMessageList(List<Message> messageList) { this.messageList = messageList; }

}