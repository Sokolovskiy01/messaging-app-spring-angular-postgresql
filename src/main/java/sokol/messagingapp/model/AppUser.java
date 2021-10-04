package sokol.messagingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name; // stands for name and surname or custom nickname
    private LocalDate dateOfBirth;
    private String gender;
    @Column(length = 1024)
    private String imageUrl;
    @Column(length = 1024)
    private String comment;

    /* Login credentials */
    @Column(nullable = false, updatable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    /* Login credentials */

    @Column(nullable = false)
    private UserStatus userStatus;
    private String banMessage; // reason of blocking user if he's banned
    private LocalDateTime lastLogin;

    public AppUser() {}

    public AppUser(String name, LocalDate dateOfBirth, String gender, String imageUrl, String comment, String email, String password, UserStatus userStatus, String banMessage, LocalDateTime lastLogin) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.email = email;
        this.password = password;
        this.userStatus = userStatus;
        this.banMessage = banMessage;
        this.lastLogin = lastLogin;
    }

    /* Getters and setters */

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserStatus getUserStatus() { return userStatus; }
    public void setUserStatus(UserStatus userStatus) { this.userStatus = userStatus; }

    public String getBanMessage() { return banMessage; }
    public void setBanMessage(String banMessage) { this.banMessage = banMessage; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String toString() {
        return "User id: " + this.id + "\n" +
                "Name: " + this.name + "\n" +
                "Date of birth: " + this.dateOfBirth + "\n" +
                "Gender: " + this.gender + "\n" +
                "ImageUser: " + this.imageUrl + "\n" +
                "Comment: " + this.comment + "\n" +
                "Email: " + this.email + "\n" +
                "Password: " + this.password + "\n" +
                "UserStatus: " + this.userStatus + "\n" +
                "BanMessage: " + this.banMessage + "\n" +
                "LastLogin: " + this.lastLogin;
    }

}