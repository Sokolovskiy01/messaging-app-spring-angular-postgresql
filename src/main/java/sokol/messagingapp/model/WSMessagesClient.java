package sokol.messagingapp.model;

public class WSMessagesClient {

    private String user;
    private Long userId;
    private Long chatId;

    public WSMessagesClient(String user, Long userId, Long chatId) {
        this.user = user;
        this.userId = userId;
        this.chatId = chatId;
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
