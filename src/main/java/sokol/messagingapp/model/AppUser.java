package sokol.messagingapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "app_users")
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String gender;
    @Column(nullable = false)
    private String email;
    private String imageUrl;
    private String comment;

    @Column(nullable = false)
    private UserStatus userStatus;
    private String banMessage; // reason of blocking user if he's banned
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "appUser")
    private List<UsersChats> userChats;

    public AppUser() {}

    public AppUser(String name, String surname, LocalDate dateOfBirth, String gender, String email, String imageUrl, String comment, UserStatus userStatus, String banMessage, LocalDateTime lastLogin) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.userStatus = userStatus;
        this.banMessage = banMessage;
        this.lastLogin = lastLogin;
    }

    /* Getters and setters */

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public UserStatus getUserStatus() { return userStatus; }
    public void setUserStatus(UserStatus userStatus) { this.userStatus = userStatus; }

    public String getBanMessage() { return banMessage; }
    public void setBanMessage(String banMessage) { this.banMessage = banMessage; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public List<UsersChats> getUserChats() { return userChats; }
    public void setUserChats(List<UsersChats> userChats) { this.userChats = userChats; }

}

enum UserStatus {
    Active, // normal user which can chat with everyone
    Limited, // user that got many reports and not allowed to chat with people he's not friends with
    Banned // user that has no longer access to app
}