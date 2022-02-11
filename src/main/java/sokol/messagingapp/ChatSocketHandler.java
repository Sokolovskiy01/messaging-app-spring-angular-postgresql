package sokol.messagingapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.model.WSChatClient;
import sokol.messagingapp.model.WSMessagesClient;
import sokol.messagingapp.service.ChatService;
import sokol.messagingapp.service.WSNotificationService;

import java.util.*;

@Controller
public class ChatSocketHandler {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final WSNotificationService wsNotificationService;

    public ChatSocketHandler(ChatService chatService, WSNotificationService wsNotificationService) {
        this.chatService = chatService;
        this.wsNotificationService = wsNotificationService;
    }

    @MessageMapping("/chatsCheck")
    public void chatCheck(SimpMessageHeaderAccessor sha, Map<String, Object> message) {
        Long userId = ((Number) message.get("userId")).longValue();
        Map<String, Object> answer = new HashMap<>();
        boolean a = chatService.hasAppUserUnreadChats(userId);
        answer.put("answer", a);
        answer.put("user", sha.getUser().getName());
        simpMessagingTemplate.convertAndSendToUser(sha.getUser().getName(), this.wsNotificationService.chatsResponseDestination, answer);
        this.wsNotificationService.addChatSubscription(new WSChatClient(sha.getUser().getName(), userId));
    }

    @MessageMapping("/messagesCheck")
    public void messagesCheck(SimpMessageHeaderAccessor sha, Map<String, Object> message) {
        Long userId = ((Number) message.get("userId")).longValue();
        Long chatId = ((Number) message.get("chatId")).longValue();
        Map<String, Object> answer = new HashMap<>();
        boolean a = chatService.hasAppUserUnreadChat(userId, chatId);
        answer.put("answer", a);
        answer.put("message", null);
        answer.put("user", sha.getUser().getName());
        simpMessagingTemplate.convertAndSendToUser(sha.getUser().getName(), this.wsNotificationService.messagesResponseDestination, answer);
        this.wsNotificationService.addMessagesSubscription(new WSMessagesClient(sha.getUser().getName(), userId, chatId));
    }

    /**
     * Stop sending chat updates to AppUser if he exited app or not seeing chats component
     * @param sha Headers of message
     * @param message Json object
     */
    @MessageMapping("/chatsUnsubscribe")
    public void unsubscribeChat(SimpMessageHeaderAccessor sha, Map<String, Object> message) {
        String user = sha.getUser().getName();
        this.wsNotificationService.unsubscribeChat(user);
    }

    /**
     * Stop sending messages to AppUser if he exited app or not seeing any of chat's messages
     * @param sha Headers of message
     * @param message Json object
     */
    @MessageMapping("/messagesUnsubscribe")
    public void unsubscribeMessages(SimpMessageHeaderAccessor sha, Map<String, Object> message) {
        String user = sha.getUser().getName();
        this.wsNotificationService.unsubscribeMessages(user);
    }

}
