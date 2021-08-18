package sokol.messagingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokol.messagingapp.model.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {

    List<Chat> findAllByUser1IdOrUser2Id(Long user1Id, Long user2Id); // get all app user's chats
    Optional<Chat> findByUser1IdAndUser2Id(Long user1Id, Long user2Id); // if chat exists
    //List<Chat> findAllByIdIsIn(List<Long> chatIds);

}
