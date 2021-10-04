package sokol.messagingapp.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import sokol.messagingapp.model.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppUserDTO {

    private Long id;
    private String name;

    private LocalDate dateOfBirth;
    private String gender;
    private String imageUrl;
    private String comment;

    private String email;

    private UserStatus userStatus;
    private String banMessage;
    private LocalDateTime lastLogin;

    public AppUserDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public UserStatus getUserStatus() { return userStatus; }
    public void setUserStatus(UserStatus userStatus) { this.userStatus = userStatus; }

    public String getBanMessage() { return banMessage; }
    public void setBanMessage(String banMessage) { this.banMessage = banMessage; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

}
