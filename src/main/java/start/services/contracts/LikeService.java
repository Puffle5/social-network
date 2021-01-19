package start.services.contracts;

import start.entities.Like;
import start.entities.User;

import java.util.List;

public interface LikeService {
    List<Like> getALLLikes();

    Like getLikeById(Integer id);

    void deleteLikeById(Integer id);

    Like likePosts(Integer postId);

    Like checkIsLike(User loginUser, Integer postId);

    List<Like> getAllLikesByPost(Integer postId);
}
