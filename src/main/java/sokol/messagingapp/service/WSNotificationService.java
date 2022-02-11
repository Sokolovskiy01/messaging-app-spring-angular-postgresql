package sokol.messagingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import sokol.messagingapp.mapstruct.dtos.MessageDTO;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.model.WSChatClient;
import sokol.messagingapp.model.WSMessagesClient;

import java.util.*;

@Service
public class WSNotificationService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public final String chatsResponseDestination = "/userMessages/chats";
    public final String messagesResponseDestination = "/userMessages/messages";

    List<WSChatClient> subscribedChats;
    List<WSMessagesClient> subscribedMessages;

    public WSNotificationService() {
        this.subscribedChats = new ArrayList<>();
        this.subscribedMessages = new ArrayList<>();
    }

    public void addChatSubscription(WSChatClient wsChatClient) {
        for (WSChatClient wscc : this.subscribedChats) {
            if (Objects.equals(wscc.getUser(), wsChatClient.getUser())) {
                //this.printSubscribedChats();
                return;
            }
        }
        this.subscribedChats.add(wsChatClient);
        //this.printSubscribedChats();
    }

    public void addMessagesSubscription(WSMessagesClient wsMessagesClient) {
        for (WSMessagesClient wsmc : this.subscribedMessages) {
            if (Objects.equals(wsmc.getUser(), wsMessagesClient.getUser())) {
                //this.printSubscribedMessages();
                return;
            }
        }
        this.subscribedMessages.add(wsMessagesClient);
        //this.printSubscribedMessages();
    }

    public void unsubscribeChat(String user) {
        List<WSChatClient> tmp = new ArrayList<>();
        this.subscribedChats.stream().filter(sc -> Objects.equals(sc.getUser(), user)).forEach(tmp::add);
        this.subscribedChats.removeAll(tmp);
        System.out.println("Chat for user " + user + " was unsubscribed");
    }

    public void unsubscribeMessages(String user) {
        List<WSMessagesClient> tmp = new ArrayList<>();
        this.subscribedMessages.stream().filter(sc -> Objects.equals(sc.getUser(), user)).forEach(tmp::add);
        this.subscribedMessages.removeAll(tmp);
        System.out.println("Messages for user " + user + " was unsubscribed");
    }

    public void sendSignalToChat(Long userId) {
        for (WSChatClient wscc : this.subscribedChats) {
            if (Objects.equals(wscc.getUserId(), userId)) {
                Map<String, Object> answer = new HashMap<>();
                answer.put("answer", true);
                answer.put("user", wscc.getUser());
                simpMessagingTemplate.convertAndSendToUser(wscc.getUser(), this.chatsResponseDestination, answer);
                System.out.println("Sent message to " + wscc.getUserId());
            }
        }
        System.out.println("No subscribed chats with userId : " + userId);
    }

    public void sendSignalToMessages(Long userId, Long chatId, MessageDTO message) {
        for (WSMessagesClient wsmc : this.subscribedMessages) {
            if (Objects.equals(wsmc.getChatId(), chatId) && Objects.equals(wsmc.getUserId(), userId)) {
                Map<String, Object> answer = new HashMap<>();
                answer.put("answer", true);
                answer.put("message", message);
                answer.put("user", wsmc.getUser());
                simpMessagingTemplate.convertAndSendToUser(wsmc.getUser(), this.messagesResponseDestination, answer);
                System.out.println("Sent message to " + wsmc.getUserId() + " with chatId: " + wsmc.getChatId());
            }
        }
        System.out.println("No subscribed messages with chatId: " + chatId);
    }

    private void printSubscribedChats() {
        System.out.println("Subscribed chats:");
        for (WSChatClient wscc : this.subscribedChats) {
            System.out.println(wscc.getUser() + ", userId: " + wscc.getUserId());
        }
    }

    private void printSubscribedMessages() {
        System.out.println("Subscribed messages:");
        for (WSMessagesClient wsmc : this.subscribedMessages) {
            System.out.println(wsmc.getUser() + ", userId: " + wsmc.getUserId() + ", chatId: " + wsmc.getChatId());
        }
    }

}
