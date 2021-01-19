package start.controllers.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import start.entities.User;
import start.entities.models.UserBindingModel;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.find.dir.BuildFileDir;
import start.find.dir.CheckFileExtension;
import start.find.login.user.FindLoginUser;
import start.services.contracts.UserService;

import java.util.List;

@RestController()
@RequestMapping("/api/users")
@Api(value = "UserRESTController", produces =
        MediaType.APPLICATION_JSON_VALUE)
public class UserRESTController {

    private UserService userService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    @Autowired
    public UserRESTController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/update-user", produces = "application/json")
    public User updateUser(@RequestBody UserBindingModel userBindingModel) {

        try {
            return userService.updateInfoUser(userBindingModel, userService.getUserByName(FindLoginUser.findLoginUserName()));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/")
    public List<User> allUsers() {
        try {
            return userService.getAllUsers(FindLoginUser.findLoginUserName());
        } catch (NotAuthorizedException not) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @GetMapping("/without-login-user")
    public List<User> allUsersWithoutLoginUser() {
        try {
            User loginUser = new User();
            String loginUserName = FindLoginUser.findLoginUserName();
            List<User> users = userService.getAllUsers(loginUserName);
            for (User crr : users) {
                if (crr.getUsername().equals(loginUserName)) {
                    loginUser = crr;
                }
            }

            users.remove(loginUser);
            return users;

        } catch (NotAuthorizedException not) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @GetMapping("/names")
    public List<String> allUsersNames() {
        try {
            return userService.getAllUsersName();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/{name}")
    public User getUserByName(@PathVariable String name) {
        try {
            return userService.getUserByName(name);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public User getUserByName(@PathVariable Integer id) {
        try {
            return userService.getUserById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/emails")
    public List<String> getUserEmails() {
        try {
            return userService.getAllUserEmails();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{username}")
    public void deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/login-user")
    public User getLoginUser() {
        try {
            return userService.loginUser(FindLoginUser.findLoginUserName());
        }  catch (NotAuthorizedException not){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping("/uploadAction/profile-picture")
    public void uploadProfilePicture(@RequestParam("files") MultipartFile[] files) {

        try {
            String fileNames = BuildFileDir.getFileName(files).toString().toLowerCase();
            if (CheckFileExtension.checkExtension(CheckFileExtension.getFileExtension(fileNames))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Upload photo");
            }
            User user = userService.getUserByName(FindLoginUser.findLoginUserName());

            user.setPhotoURL(fileNames);
            userService.save(user);
        } catch (NotAuthorizedException not) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/uploadAction/cover-picture")
    public void upload(@RequestParam("files") MultipartFile[] files) {

        try {
            String fileNames = BuildFileDir.getFileName(files).toString().toLowerCase();
            if (CheckFileExtension.checkExtension(CheckFileExtension.getFileExtension(fileNames))){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Upload photo");
            }

            User user = userService.loginUser(FindLoginUser.findLoginUserName());
            user.setCoverPhotoURL(fileNames);
            userService.save(user);
        } catch (NotAuthorizedException not) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

}
