package sokol.messagingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokol.messagingapp.model.UsersChats;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatToUserRepo extends JpaRepository<UsersChats, Long> {

    List<UsersChats> findUsersChatsByAppUserId(Long userId);
    Optional<UsersChats> findUsersChatsByChatIdAndAppUserId(Long chatId, Long appUserId);

}
