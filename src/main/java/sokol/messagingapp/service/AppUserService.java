package sokol.messagingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sokol.messagingapp.exception.PasswordException;
import sokol.messagingapp.exception.UserExistsException;
import sokol.messagingapp.exception.UserNotFoundException;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.UserStatus;
import sokol.messagingapp.repo.AppUserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AppUser createUser(AppUser newUser) {
        Optional<AppUser> existingUser = userRepo.findAppUserByEmail(newUser.getEmail());
        if (existingUser.isEmpty()) {
            newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
            return userRepo.save(newUser);
        }
        else throw new UserExistsException("User with email " + newUser.getEmail() + " exists");
    }

    public List<AppUser> getAllUsers() {
        return userRepo.findAll();
    }

    public List<AppUser> getUsersWithContainedQueryInName(String query) {
        return userRepo.findAllByNameContains(query);
    }

    public AppUser getAppUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with following id: " + id + " was not found"));
    }

    public AppUser loginAppUser(String email, String password) {
        Optional<AppUser> existingUser = userRepo.findAppUserByEmail(email);
        if (existingUser.isEmpty()) {
            throw new UserExistsException("User with email " + email + " does not exists");
        }
        else {
            if (this.passwordEncoder.matches(password, existingUser.get().getPassword())) {
                return existingUser.get();
            }
            else {
                throw new PasswordException("Password is not correct");
            }
        }
    }

    public AppUser updateUser(AppUser newAppUser) {
        // retrieve previous password because before sending AppUser to frontend password sets to null
        AppUser prevAppUser = userRepo.getById(newAppUser.getId());
        newAppUser.setPassword(prevAppUser.getPassword());
        return userRepo.save(newAppUser);
    }

    public AppUser updateUserPassword(AppUser appUser, String newPassword) {
        if (passwordEncoder.matches(newPassword, appUser.getPassword())) {
            throw new PasswordException("New password can't be equal to the old one");
        }
        else {
            appUser.setPassword(passwordEncoder.encode(newPassword));
            return userRepo.save(appUser);
        }
    }

    public AppUser banAppUser(AppUser appUser, String banMessage) {
        appUser.setUserStatus(UserStatus.Banned);
        appUser.setBanMessage(banMessage);
        // TODO : delete saved user session
        return userRepo.save(appUser);
    }

    public AppUser limitAppUser(AppUser appUser) {
        appUser.setUserStatus(UserStatus.Limited);
        return userRepo.save(appUser);
    }

    public void deleteAppUser(Long userId) {
        userRepo.deleteAppUserById(userId);
    }

}
