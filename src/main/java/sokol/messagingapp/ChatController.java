package sokol.messagingapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.service.AppUserService;
import sokol.messagingapp.service.ChatService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final AppUserService appUserService;

    public ChatController(ChatService chatService, AppUserService appUserService) {
        this.chatService = chatService;
        this.appUserService = appUserService;
    }

    @GetMapping("/get/{chatId}")
    public ResponseEntity<Chat> getChat(@PathVariable("chatId") Long chatId) {
        Chat existingChat = chatService.getChatById(chatId);
        return new ResponseEntity<>(existingChat, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getUserChats(@PathVariable("userId") Long userid) {
        List<Chat> userChats = chatService.getAllAppUserChats(userid);
        return new ResponseEntity<>(userChats, HttpStatus.OK);
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable("chatId") Long chatId, @Nullable @RequestParam("userid") Long userid) {
        List<Message> chatMessages;
        try {
            if (userid != null) chatMessages = chatService.getChatMessagesAsAppUser(chatId, userid);
            else chatMessages = chatService.getChatMessages(chatId);
            return new ResponseEntity<>(chatMessages, HttpStatus.OK);
        }
        catch (RuntimeException re) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(@RequestBody Map<String, Object> obj) {
        Long user1Id = ((Number) obj.get("user1Id")).longValue();
        Long user2Id = ((Number) obj.get("user2Id")).longValue();
        AppUser user1 = appUserService.getAppUserById(user1Id);
        AppUser user2 = appUserService.getAppUserById(user2Id);
        Chat chat = chatService.createChat(user1, user2);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /* send message and receive list of all messages in chat */
    @PostMapping("/sendmessage")
    public ResponseEntity<List<Message>> sendMessage(@RequestBody Map<String, Object> messageObject) {
        Long chatId = ((Number) messageObject.get("chatId")).longValue();
        Long userId = ((Number) messageObject.get("userId")).longValue();
        String messageContent = (String) messageObject.get("message");
        /* Message message = */ chatService.sendMessage(chatId, userId, messageContent);
        List<Message> chatMessages = chatService.getChatMessages(chatId);
        return new ResponseEntity<>(chatMessages, HttpStatus.OK);
    }

}
