package sokol.messagingapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{chatid}")
    public ResponseEntity<Chat> getChat(@PathVariable("chatid") Long chatId) {
        Chat existingChat = chatService.getChatById(chatId);
        return new ResponseEntity<>(existingChat, HttpStatus.OK);
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<Chat>> getUserchats(@PathVariable("userid") Long userid) {
        List<Chat> userChats = chatService.getAllAppUserChats(userid);
        return new ResponseEntity<>(userChats, HttpStatus.OK);
    }
}
