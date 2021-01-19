package start.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import start.entities.Comment;
import start.entities.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> getAllByActiveTrue();

    List<Comment> getAllByPostAndActiveTrue(Post post);

    Comment getByIDAndActiveTrue(Integer id);
}
