package sokol.messagingapp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sokol.messagingapp.exception.PasswordException;
import sokol.messagingapp.exception.UserExistsException;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.UserStatus;
import sokol.messagingapp.service.AppUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{userid}")
    public ResponseEntity<AppUser> getAppUser(@PathVariable("userid") Long id) {
        AppUser requestUser = appUserService.getAppUserById(id);
        return new ResponseEntity<>(requestUser, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppUser>> getAppUsers(@RequestParam String q, @RequestParam int uid) {
        List<AppUser> searchUsers = appUserService.getUsersWithContainedQueryInName(q, uid);
        for (AppUser au: searchUsers) {
            au.setPassword(null);
        }
        return new ResponseEntity<>(searchUsers, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createAppUser(@RequestBody AppUser appUser) {
        AppUser newAppUser;
        try { newAppUser = appUserService.createUser(appUser); }
        catch (UserExistsException uex) { return new ResponseEntity<>(uex.getMessage(), HttpStatus.NOT_ACCEPTABLE); }
        return new ResponseEntity<>(newAppUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginAppUser(@RequestBody Map<String, Object> loginObject) {
        String email = (String) loginObject.get("email");
        String password = (String) loginObject.get("password");
        try {
            AppUser appUser = appUserService.loginAppUser(email, password);
            appUser.setPassword(null);
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }
        catch (UserExistsException | PasswordException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<AppUser> updateAppUser(@RequestBody AppUser appUser) {
        // TODO try-catch as in /login
        AppUser updatedAppUser = appUserService.updateUser(appUser);
        return new ResponseEntity<>(updatedAppUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userid}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable("userid") Long id) {
        try {
            appUserService.deleteAppUser(id);
            return new ResponseEntity<>("User with id: " + id + " was permanently deleted", HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Unable to delete user with id: " + id, HttpStatus.NOT_MODIFIED);
        }

    }

}
