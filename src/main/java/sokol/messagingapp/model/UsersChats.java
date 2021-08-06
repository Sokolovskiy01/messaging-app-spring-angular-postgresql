package sokol.messagingapp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "users_chats")
public class UsersChats implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName="id", nullable=false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName="id", nullable=false)
    private AppUser appUser;

    @Column(nullable = false)
    private boolean seen; // to notify each user if another chat member sent new message

    public UsersChats() {}

    public UsersChats(Chat chat, AppUser appUser, boolean seen) {
        this.chat = chat;
        this.appUser = appUser;
        this.seen = seen;
    }

    public Long getId() { return id; }

    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public AppUser getAppUser() { return appUser; }
    public void setAppUser(AppUser appUser) { this.appUser = appUser; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
}

/*
 To create new chat:
    1) Create chat entity in chat table
    2) Create user-to-chat connection in users_chats table
*/

/*
 To post a message:
    1) Create new message in messages table
    2) Update users_chats with recipient id seen value to false
*/