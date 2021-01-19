package start.controllers.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import start.entities.User;
import start.exceptions.EntityNotFoundException;
import start.services.contracts.RelationshipService;
import start.services.contracts.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserService userService;
    private RelationshipService relationshipService;

    @Autowired
    public ProfileController(UserService userService, RelationshipService relationshipService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    @GetMapping
    String profile(Principal principal, Model model) {
        try {
            User user = userService.getUserByName(principal.getName());
            model.addAttribute("user", user);
            return "profile";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/settings")
    String profileAccountSettings(Principal principal, Model model) {
        User user = userService.getUserByName(principal.getName());
        model.addAttribute("user", user);
        return "account-settings";
    }

    @GetMapping("/new-feeds")
    String showNewFeeds() {
        return "new-feeds";
    }

    @GetMapping("/user/{username}")
    String showPersonProfilePage(@PathVariable String username, Model model) {
        try {
            User user = userService.getUserByName(username);
            model.addAttribute("user", user);
            return "other-user-page";
        }catch (EntityNotFoundException e){
            return "error";
        }
    }


    @GetMapping("/user/decline/{username}")
    String declinePersonFriendRequest(@PathVariable String username) {
        relationshipService.declineFriendRequest(username);
        return "redirect:/profile/user/" + username;
    }

    @GetMapping("/user/delete/{username}")
    String deleteUserByName(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/admin";
    }

}
