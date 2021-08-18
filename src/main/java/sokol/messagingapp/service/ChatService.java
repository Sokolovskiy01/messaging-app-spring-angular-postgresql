package sokol.messagingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sokol.messagingapp.exception.ChatNotFoundException;
import sokol.messagingapp.exception.MessageNotFoundException;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.repo.AppUserRepo;
import sokol.messagingapp.repo.ChatRepo;
import sokol.messagingapp.repo.MessageRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final AppUserRepo appUserRepo;

    @Autowired
    public ChatService(ChatRepo chatRepo, MessageRepo messageRepo, AppUserRepo appUserRepo) {
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
        this.appUserRepo = appUserRepo;
    }

    public Chat getChatById(Long id) {
        return chatRepo.findById(id).orElseThrow(() -> new ChatNotFoundException("Chat by provided id: " + id + " does not exist"));
    }

    public Message getMessageById(Long id) {
        return messageRepo.findById(id).orElseThrow(() -> new MessageNotFoundException("Message with id: " + id + " does not exist"));
    }

    public List<Message> getChatMessages(Long chatId) {
        Optional<Chat> chat = chatRepo.findById(chatId);
        if (chat.isEmpty()) throw new RuntimeException("Chat does not exist");
        else return messageRepo.findAllByChat(chat.get());
    }

    public Chat createChat(AppUser user1, AppUser user2) {
        Optional<Chat> existingChat = chatRepo.findByUser1IdAndUser2Id(user1.getId(), user2.getId());
        if (existingChat.isEmpty()) {
            return chatRepo.save(new Chat(user1, user2, true, false));
        }
        else return existingChat.get();
    }

    public Message sendMessage(Long chatId, Long userId, String messageContent) {
        // notify all other users that new message was sent
        Chat chat = chatRepo.getById(chatId);
        AppUser user = appUserRepo.getById(userId);
        Message message = new Message(chat, user, messageContent, LocalDateTime.now());
        return messageRepo.save(message);
    }

    /*public UsersChats getUsersChatsById(Long id) {
        return chatToUserRepo.findUsersChatsByChatIdAndAppUserId(id).orElseThrow(() -> new ChatNotFoundException("User to chat connection with id: " + id + " does not exist"));
    }*/

    public List<Chat> getAllAppUserChats(Long userId) {
        List<Chat> usersChats = chatRepo.findAllByUser1IdOrUser2Id(userId, userId);
        return usersChats;
    }

    public Chat updateChat(Chat chat) {
        return chatRepo.save(chat);
    }

    /*public void deleteChat(Chat chat) {
        chatRepo.deleteChatById(chat.getId());
    }*/


}
