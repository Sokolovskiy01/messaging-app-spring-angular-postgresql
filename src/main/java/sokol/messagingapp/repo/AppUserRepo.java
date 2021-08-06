package sokol.messagingapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sokol.messagingapp.model.AppUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findAppUserByEmail(String email);
    List<AppUser> findAllByNameContains(String substring);
    void deleteAppUserById(Long Id);

}
