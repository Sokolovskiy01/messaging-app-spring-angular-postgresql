package sokol.messagingapp.mapstruct.dtos;

public class ChatDTO {

    private Long id;
    private Long user1;
    private Long user2;

    private boolean user1Seen;
    private boolean user2Seen;

    public ChatDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUser1() { return user1; }
    public void setUser1(Long user1) { this.user1 = user1; }

    public Long getUser2() { return user2; }
    public void setUser2(Long user2) { this.user2 = user2; }

    public boolean isUser1Seen() { return user1Seen; }
    public void setUser1Seen(boolean user1Seen) { this.user1Seen = user1Seen; }

    public boolean isUser2Seen() { return user2Seen; }
    public void setUser2Seen(boolean user2Seen) { this.user2Seen = user2Seen; }

}
