package start.services.contracts;

import start.entities.Comment;
import start.entities.Post;
import start.entities.User;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments();

    Comment getCommentById(Integer id);

    List<Comment> getAllCommentsByPost(Post post);

    Comment createComment(String commentDescription, User loginUser, Post postForCommenting);

    void deleteComment(Integer id);
}
