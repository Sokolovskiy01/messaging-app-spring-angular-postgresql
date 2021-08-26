package sokol.messagingapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sokol.messagingapp.model.AppUser;
import sokol.messagingapp.model.UserStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

// нихуя не работает
@Controller
public class AppUserSessionController {

    /*@GetMapping("/init")
    public ResponseEntity<String> initSession(Model model, HttpSession session) {
        AppUser sessionUser = (AppUser) session.getAttribute("loggedAs");
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("loggedAs", 1948);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PostMapping("/setUser")
    public ResponseEntity<String> appendSessionData(@RequestBody Map<String, Object> obj, HttpServletRequest request) {
        Integer loggedAS = (Integer) request.getSession().getAttribute("loggedAs");
        if (loggedAS == null) request.getSession().setAttribute("loggedAs", 1948);
        return new ResponseEntity<>("User was set", HttpStatus.OK);
    }*/

}
