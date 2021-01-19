package start.controllers.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import start.find.login.user.FindLoginUser;
import start.services.contracts.UserService;

import java.security.Principal;

@Controller
@RequestMapping
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin(Principal principal, Model model) {
        if (principal != null) {

            model.addAttribute("user", userService.getUserByName(FindLoginUser.findLoginUserName()));
            return "profile";
        } else {
            return "login";
        }
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "error";
    }
}
