package sokol.messagingapp.mapstruct.mappers;

import org.mapstruct.Mapper;
import sokol.messagingapp.mapstruct.dtos.AppUserDTO;
import sokol.messagingapp.mapstruct.dtos.ChatDTO;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.model.Message;
import sokol.messagingapp.mapstruct.dtos.MessageDTO;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {

    AppUserDTO appUserToAppUserDTO(AppUser appUser);
    ChatDTO chatToChatDTO(Chat chat);
    MessageDTO messageToMessageDTO(Message message);

    List<MessageDTO> messageListToMessageDTOList(List<Message> messageList);

    /* Get AppUser Id */
    default Long appUserToLong(AppUser appUser) {
        if (appUser == null) return null;
        return appUser.getId();
    }

    /* Get Chat Id */
    default Long chatToLong(Chat chat) {
        if (chat == null) return null;
        return chat.getId();
    }

}
