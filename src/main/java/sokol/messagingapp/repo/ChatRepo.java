package sokol.messagingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sokol.messagingapp.model.Chat;
import sokol.messagingapp.model.UsersChats;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {

    List<Chat> findAllByChatUsers(List<UsersChats> usersChats);
    List<Chat> findAllByIdIsIn(List<Long> chatIds);

}
