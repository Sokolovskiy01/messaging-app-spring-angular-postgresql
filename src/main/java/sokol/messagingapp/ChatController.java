package sokol.messagingapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import sokol.messagingapp.mapstruct.dtos.MessageDTO;
import sokol.messagingapp.mapstruct.mappers.MapStructMapper;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.service.AppUserService;
import sokol.messagingapp.service.ChatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final AppUserService appUserService;
    private final MapStructMapper mapStructMapper;

    public ChatController(ChatService chatService, AppUserService appUserService, MapStructMapper mapStructMapper) {
        this.chatService = chatService;
        this.appUserService = appUserService;
        this.mapStructMapper = mapStructMapper;
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
    public ResponseEntity<List<MessageDTO>> getChatMessages(@PathVariable("chatId") Long chatId, @Nullable @RequestParam("userid") Long userid) {
        List<MessageDTO> chatMessages;
        try {
            if (userid != null) chatMessages = mapStructMapper.messageListToMessageDTOList(chatService.getChatMessagesAsAppUser(chatId, userid));
            else chatMessages = mapStructMapper.messageListToMessageDTOList(chatService.getChatMessages(chatId));
            return new ResponseEntity<>(chatMessages, HttpStatus.OK);
        }
        catch (RuntimeException re) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/members/{chatId}")
    public ResponseEntity<Map<String, Object>> getChatMembers(@PathVariable("chatId") Long chatId) {
        Map<String, Object> responseBody = new HashMap<>();
        Chat chat = chatService.getChatById(chatId);
        if (chat != null) {
            responseBody.put("user1", chat.getUser1());
            responseBody.put("user2", chat.getUser2());
            responseBody.put("status", 200);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        else {
            responseBody.put("status", 404);
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
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
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody Map<String, Object> messageObject) {
        Long chatId = ((Number) messageObject.get("chatId")).longValue();
        Long userId = ((Number) messageObject.get("userId")).longValue();
        String messageContent = (String) messageObject.get("message");
        Message message =  chatService.sendMessage(chatId, userId, messageContent);
        return new ResponseEntity<>(mapStructMapper.messageToMessageDTO(message), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<Map<String, Object>> deleteChat(@PathVariable("chatId") Long chatId) {
        System.out.println("gg " + chatId);
        Map<String, Object> answer = new HashMap<>();
        boolean result = true;
        try { this.chatService.deleteChat(chatId); }
        catch (Exception e) { result = false; }
        answer.put("ok", result);
        return new ResponseEntity<>(answer, (result) ? HttpStatus.OK : HttpStatus.NOT_MODIFIED);
    }

}
