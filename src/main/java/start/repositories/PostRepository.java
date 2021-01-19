package start.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import start.entities.Post;
import start.entities.User;
import start.entities.enumerators.PostStatus;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> getAllByActiveTrue();

    Post getByIDAndActiveTrue(Integer id);

    List<Post> findAllByUserAndActiveTrueOrderByDateDesc(User user);

    List<Post> getAllByUserAndActiveTrue(User user);

    List<Post> getAllByUserAndActiveTrueAndStatusOrderByDateDesc(User user, PostStatus postStatus);
}
