package sokol.messagingapp.model;

public class WSChatClient {

    private String user;
    private Long userId;

    public WSChatClient(String user, Long userId) {
        this.user = user;
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
