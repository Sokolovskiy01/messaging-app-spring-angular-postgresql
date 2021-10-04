package sokol.messagingapp.mapstruct.dtos;

import java.time.LocalDateTime;

public class MessageDTO {

    private Long id;
    private String messageContent;
    private LocalDateTime sentDate;

    private Long chat;

    private Long sender;

    public MessageDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }

    public Long getChat() { return chat; }
    public void setChat(Long chatId) { this.chat = chatId; }

    public Long getSender() { return sender; }
    public void setSender(Long senderId) { this.sender = senderId; }

}
