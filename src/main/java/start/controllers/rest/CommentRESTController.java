package start.controllers.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import start.entities.Comment;
import start.entities.Post;
import start.entities.User;
import start.entities.models.CommentBindingModel;
import start.exceptions.EntityNotFoundException;
import start.find.login.user.FindLoginUser;
import start.replace.RemoveWhiteSpaces;
import start.services.contracts.CommentService;
import start.services.contracts.PostService;
import start.services.contracts.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Api(value = "CommentRESTController", produces =
        MediaType.APPLICATION_JSON_VALUE)
public class CommentRESTController {
    private CommentService commentService;
    private UserService userService;
    private PostService postService;

    @Autowired
    public CommentRESTController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping()
    @ApiOperation("Get all comment.")
    List<Comment> getAllComments() {
        try {
            return commentService.getAllComments();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    Comment getCommentById(@PathVariable Integer id) {
        try {
            return commentService.getCommentById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/get-post-comments/{id}")
    List<Comment> getCommentByPostId(@PathVariable Integer id) {
        try {
            Post post = postService.getPostById(id);
            return commentService.getAllCommentsByPost(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping(value = "/create-comment/{postId}",
            consumes="application/json")
    Comment createCommentBy(@RequestBody CommentBindingModel commentBindingModel, @PathVariable Integer postId) {
        try {
            String description = RemoveWhiteSpaces.removeWhiteSpaces(commentBindingModel.getDescription());
            description = RemoveWhiteSpaces.removeHTMLTags(description);
            User loginUser = userService.getUserByName(FindLoginUser.findLoginUserName());
            Post postForCommenting = postService.getPostById(postId);
            return commentService.createComment(description, loginUser, postForCommenting);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete-comment/{id}")
    void deleteCommentById(@PathVariable Integer id) {
        try {
            commentService.deleteComment(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
