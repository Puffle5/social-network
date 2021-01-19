package start.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import start.entities.Like;
import start.entities.Post;
import start.entities.User;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> getAllByActiveTrue();

    Like getLikeByIDAndActiveTrue(Integer id);

    Like getLikeByUserAndPost(User user, Post post);

    List<Like> getAllByPost(Post post);
}
