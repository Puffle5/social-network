package start.controllers.mvc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import start.entities.User;
import start.replace.RemoveWhiteSpaces;
import start.services.contracts.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class RegistrationController {
    private UserDetailsManager userDetailsManager;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserDetailsManager userDetailsManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, Principal principal) {
        if (principal != null) {
            return "home";
        } else {
            model.addAttribute("user", new User());
            return "register";
        }

    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) throws Exception {
        String username = RemoveWhiteSpaces.removeWhiteSpaces(user.getUsername());

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Username/password can't be less than 3 symbols.");
            return "register";
        }

        if (userDetailsManager.userExists(username) || userDetailsManager.userExists(username + "deleteUser")) {
            model.addAttribute("error", "User with same username already exists!");
            return "register";
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(
                        username,
                        passwordEncoder.encode(user.getPassword()),
                        authorities);
        userDetailsManager.createUser(newUser);

        User currentUser = userService.getUnderTheHoodUser(username);

        currentUser.setEmail(user.getEmail());
        currentUser.setCoverPhotoURL("default_cover.jpg");
        currentUser.setPhotoURL("default_profile.jpg");
        currentUser.setActive(true);

        userService.save(currentUser);
        return "login";
    }

    @GetMapping("/register-confirmation")
    public String registerConfirmation() {
        return "login";
    }
}