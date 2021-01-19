package start.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import start.entities.Like;
import start.entities.Post;
import start.entities.User;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.find.login.user.FindLoginUser;
import start.repositories.LikeRepository;
import start.repositories.PostRepository;
import start.repositories.UserRepository;
import start.services.contracts.LikeService;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Like> getALLLikes() {
        List<Like> likes = likeRepository.getAllByActiveTrue();

        if (CollectionUtils.isEmpty(likes)) {
            throw new EntityNotFoundException("There are no likes.");
        }

        return likes;
    }

    public Like getLikeById(Integer id) {
        Like like = likeRepository.getLikeByIDAndActiveTrue(id);

        if (like == null) {
            throw new EntityNotFoundException("There is no like.");
        }

        return like;
    }

    public void deleteLikeById(Integer id) {
        Like like = likeRepository.getLikeByIDAndActiveTrue(id);

        if (like == null) {
            throw new EntityNotFoundException("There is no like for deleting.");
        }

        like.setActive(false);

        likeRepository.saveAndFlush(like);
    }

    @Override
    public Like likePosts(Integer postId) {
        Post post = postRepository.getByIDAndActiveTrue(postId);
        User loginUser = userRepository.getByUsernameAndActiveTrue(FindLoginUser.findLoginUserName());
        if (loginUser == null){
            throw new NotAuthorizedException("You are not authorized!");
        }
        if (post == null) {
            throw new EntityNotFoundException();
        }

        Like like = likeRepository.getLikeByUserAndPost(loginUser, post);

        if (like == null) {
            like = new Like();
            like.setActive(true);
            like.setPost(post);
            like.setUser(loginUser);

            return likeRepository.saveAndFlush(like);
        } else {
            likeRepository.delete(like);
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Like checkIsLike(User loginUser, Integer postId) {
        Post post = postRepository.getByIDAndActiveTrue(postId);
        Like like = likeRepository.getLikeByUserAndPost(loginUser, post);

        if (like == null) {
            throw new EntityNotFoundException();
        }
        return like;
    }

    @Override
    public List<Like> getAllLikesByPost(Integer postId) {
        Post post = postRepository.getByIDAndActiveTrue(postId);
        List<Like> likes = likeRepository.getAllByPost(post);
        if (post == null || CollectionUtils.isEmpty(likes)) {
            throw new EntityNotFoundException();
        }
        return likes;
    }

}
