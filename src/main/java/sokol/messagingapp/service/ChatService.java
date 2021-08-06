package sokol.messagingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sokol.messagingapp.exception.ChatNotFoundException;
import sokol.messagingapp.exception.MessageNotFoundException;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.model.UsersChats;
import sokol.messagingapp.repo.ChatRepo;
import sokol.messagingapp.repo.ChatToUserRepo;
import sokol.messagingapp.repo.MessageRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepo chatRepo;
    private final ChatToUserRepo chatToUserRepo;
    private final MessageRepo messageRepo;

    @Autowired
    public ChatService(ChatRepo chatRepo, ChatToUserRepo chatToUserRepo, MessageRepo messageRepo) {
        this.chatRepo = chatRepo;
        this.chatToUserRepo = chatToUserRepo;
        this.messageRepo = messageRepo;
    }

    public Chat createChat(Chat chat, AppUser user1, AppUser user2) {
        // user1 created chat
        UsersChats user1ChatLink = new UsersChats(chat, user1, true);
        UsersChats user2ChatLink = new UsersChats(chat, user2, false);
        chatToUserRepo.save(user1ChatLink);
        chatToUserRepo.save(user2ChatLink);
        return chatRepo.save(chat);
    }

    public Message sendMessage(Chat chat, AppUser appUser, Message message) {
        // notify all other users that new message was sent
        List<UsersChats> chatUsers = chat.getChatUsers();
        for (UsersChats uc : chatUsers) {
            if (uc.getAppUser().getId() != appUser.getId()) {
                uc.setSeen(false);
                chatToUserRepo.save(uc);
            }
        }
        return messageRepo.save(message);
    }

    public Chat getChatById(Long id) {
        return chatRepo.findById(id).orElseThrow(() -> new ChatNotFoundException("Chat by provided id: " + id + " does not exist"));
    }

    public Message getMessageById(Long id) {
        return messageRepo.findById(id).orElseThrow(() -> new MessageNotFoundException("Message with id: " + id + " does not exist"));
    }

    public UsersChats getUsersChatsById(Long id) {
        return chatToUserRepo.findById(id).orElseThrow(() -> new ChatNotFoundException("User to chat connection with id: " + id + " does not exist"));
    }

    public List<Chat> getAllAppUserChats(Long userId) {
        List<UsersChats> usersChats = chatToUserRepo.findUsersChatsByAppUserId(userId);
        List<Long> chatIds = new ArrayList<>();
        for (UsersChats uc : usersChats) {
            chatIds.add(uc.getChat().getId());
        }
        return chatRepo.findChatsById(chatIds);
    }



    public Chat updateChat(Chat chat) {
        return chatRepo.save(chat);
    }

    public void deleteChat(Chat chat) {
        chatRepo.deleteChatById(chat.getId());
    }


}
