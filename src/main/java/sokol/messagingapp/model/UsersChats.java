package sokol.messagingapp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "users_chats")
public class UsersChats implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName="id", nullable=false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName="id", nullable=false)
    private AppUser appUser;

    @Column(nullable = false)
    private boolean seen; // to notify each user if another chat member sent new message

}

/*
 To create new chat:
    1) Create chat entity in chat table
    2) Create user-to-chat connection in users_chats table
*/