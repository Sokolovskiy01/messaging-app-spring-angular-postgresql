package sokol.messagingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokol.messagingapp.model.Chat;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {

    /*List<Chat> findChatsById(List<Long> chatIds);
    void deleteChatById(Long id);*/
}
