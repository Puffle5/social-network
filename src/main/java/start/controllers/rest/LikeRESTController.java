package start.controllers.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import start.entities.Like;
import start.exceptions.EntityNotFoundException;
import start.find.login.user.FindLoginUser;
import start.services.contracts.LikeService;
import start.services.contracts.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/likes")
@Api(value = "LikeRESTController", produces =
        MediaType.APPLICATION_JSON_VALUE)
public class LikeRESTController {

    private LikeService likeService;
    private UserService userService;

    @Autowired
    public LikeRESTController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    @GetMapping
    List<Like> getAllLikes() {
        try {
            return likeService.getALLLikes();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    Like getLikeById(@PathVariable Integer id) {
        try {
            return likeService.getLikeById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/check-is-liked/{postId}")
    Like checkIsLiked(@PathVariable Integer postId) {
        try {
            return likeService.checkIsLike(userService.getUserByName(FindLoginUser.findLoginUserName()), postId);
        } catch (EntityNotFoundException e) {
            return new Like();
        }

    }

    @GetMapping("/count-likes/{postId}")
    List<Like> countLikes(@PathVariable Integer postId) {
        try {
            return likeService.getAllLikesByPost(postId);
        } catch (EntityNotFoundException e) {
            return new ArrayList<>();
        }

    }

    @PutMapping("/like-post/{postId}")
    public Like likePost(@PathVariable Integer postId) {
        try {
            return likeService.likePosts(postId);
        } catch (EntityNotFoundException e) {
            return new Like();
        }
    }

    @DeleteMapping("/delete-like/{id}")
    void deleteLikeById(@PathVariable Integer id) {
        try {
            likeService.deleteLikeById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
