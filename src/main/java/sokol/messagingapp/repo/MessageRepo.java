package sokol.messagingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokol.messagingapp.model.Message;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

}
