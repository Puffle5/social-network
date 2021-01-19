package start.controllers.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import start.entities.Relationship;
import start.entities.User;
import start.exceptions.EntityNotFoundException;
import start.exceptions.ExistingEntityException;
import start.exceptions.NotAuthorizedException;
import start.find.login.user.FindLoginUser;
import start.services.contracts.RelationshipService;
import start.services.contracts.UserService;

import java.util.List;

@RestController()
@RequestMapping("/api/request")
@Api(value = "RequestRESTController", produces =
        MediaType.APPLICATION_JSON_VALUE)
public class RequestRESTController {

    private RelationshipService relationshipService;
    private UserService userService;

    @Autowired
    public RequestRESTController(RelationshipService relationshipService, UserService userService) {
        this.relationshipService = relationshipService;
        this.userService = userService;
    }

    @GetMapping("/personal-friend-requests")
    public List<User> getFriendRequests() {
        List<User> users;
        try {
            users = relationshipService.getAllRequestsFromUsers();
            return users;
        } catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/user/check-request/{username}")
    Relationship checkPersonFriendRequest(@PathVariable String username) {
        try {
            return relationshipService.checkFriendRequest(username);
        } catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        }catch (ExistingEntityException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/user/send-request/{username}")
    Relationship sendPersonFriendRequest(@PathVariable String username) {
        try {
            return relationshipService.sendFriendRequest(username);
        } catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        }catch (ExistingEntityException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/personal-friend")
    public List<User> getFriend() {
        List<User> users;
        try {
            users = relationshipService.getAllFriends(FindLoginUser.findLoginUserName());
            return users;
        } catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping("/accept/{username}")
    public User acceptRequest(@PathVariable String username) {
        try {
            return relationshipService.acceptFriendRequest(username);
        } catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/decline/{username}")
    public void declineRequest(@PathVariable String username) {
        try {
            relationshipService.declineFriendRequest(username);
        } catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
