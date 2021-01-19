package start.controllers.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import start.entities.Post;
import start.entities.Relationship;
import start.entities.User;
import start.entities.models.PostBindingModel;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.find.dir.BuildFileDir;
import start.find.dir.CheckFileExtension;
import start.find.login.user.FindLoginUser;
import start.replace.RemoveWhiteSpaces;
import start.services.contracts.PostService;
import start.services.contracts.RelationshipService;
import start.services.contracts.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Api(value = "PostRESTController", produces =
        MediaType.APPLICATION_JSON_VALUE)
public class PostRESTController {

    private PostService postService;
    private UserService userService;
    private RelationshipService relationshipService;

    @Autowired
    public PostRESTController(PostService postService, UserService userService, RelationshipService relationshipService) {
        this.postService = postService;
        this.userService = userService;
        this.relationshipService = relationshipService;
    }


    @GetMapping
    List<Post> getAllPosts() {
        try {
            return postService.getAllPosts();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    List<Post> getAllUserPosts(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            return postService.getAllPostByUser(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/friends/posts")
    List<Post> getAllFriendsPosts() {
        try {
            User loginUser = userService.loginUser(FindLoginUser.findLoginUserName());
            try {
                List<User> friends = relationshipService.getAllFriends(FindLoginUser.findLoginUserName());
                return postService.getAllPublicFriendsPosts(friends, loginUser);

            } catch (EntityNotFoundException e) {
                return postService.getAllPublicFriendsPosts(new ArrayList<>(), loginUser);
            }
        } catch (NotAuthorizedException not) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/{id}")
    Post getPostById(@PathVariable Integer id) {
        try {
            return postService.getPostById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/personal-posts")
    List<Post> getPersonalPosts() {
        try {
            return postService.getPersonalPosts();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete-post/{id}")
    void deletePostById(@PathVariable Integer id) {
        try {
            postService.deletePostById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping("/uploadAction/post/{title}/{description}/{status}")
    public Post uploadPostPicture(@RequestParam("files") MultipartFile[] files,
                                  @PathVariable String title,
                                  @PathVariable String description,
                                  @PathVariable String status) {
        try {
            String fileNames = BuildFileDir.getFileName(files).toString().toLowerCase();


            if (CheckFileExtension.checkExtension(CheckFileExtension.getFileExtension(fileNames))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upload photo");
            }

            if (fileNames.equals(" ")) {
                throw new IllegalArgumentException("There is no picture.");
            }


            String postTitle = RemoveWhiteSpaces.removeWhiteSpaces(title);
            postTitle = RemoveWhiteSpaces.removeHTMLTags(postTitle);
            String postDescription = RemoveWhiteSpaces.removeWhiteSpaces(description);
            postDescription = RemoveWhiteSpaces.removeHTMLTags(postDescription);

            return postService.addPost(new PostBindingModel(null, postTitle, postDescription),
                    userService.loginUser(FindLoginUser.findLoginUserName()),
                    fileNames, status);

        } catch (NotAuthorizedException not) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, not.getMessage());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
