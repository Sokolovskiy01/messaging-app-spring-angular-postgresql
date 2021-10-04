package sokol.messagingapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sokol.messagingapp.exception.PasswordException;
import sokol.messagingapp.exception.UserExistsException;
import sokol.messagingapp.mapstruct.dtos.AppUserDTO;
import sokol.messagingapp.mapstruct.mappers.MapStructMapper;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.service.AppUserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public AppUserController(AppUserService appUserService, MapStructMapper mapStructMapper) {
        this.appUserService = appUserService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{userid}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable("userid") Long id) {
        return new ResponseEntity<>(mapStructMapper.appUserToAppUserDTO(appUserService.getAppUserById(id)), HttpStatus.OK);
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
        return new ResponseEntity<>( mapStructMapper.appUserToAppUserDTO(newAppUser), HttpStatus.CREATED);
    }

    // TODO: save session data into backend and retrieve AppUser on frontend app refresh page
    @PostMapping("/login")
    public ResponseEntity<Object> loginAppUser(@RequestBody Map<String, Object> loginObject) {
        String email = (String) loginObject.get("email");
        String password = (String) loginObject.get("password");
        try {
            return new ResponseEntity<>(mapStructMapper.appUserToAppUserDTO(appUserService.loginAppUser(email, password)), HttpStatus.OK);
        }
        catch (UserExistsException | PasswordException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<AppUserDTO> updateAppUser(@RequestBody AppUser appUser) {
        // TODO try-catch as in /login
        return new ResponseEntity<>(mapStructMapper.appUserToAppUserDTO(appUserService.updateUser(appUser)), HttpStatus.OK);
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
