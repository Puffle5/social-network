package start.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import start.entities.Comment;
import start.entities.Post;
import start.entities.User;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.repositories.CommentRepository;
import start.services.contracts.CommentService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllComments() {
        List<Comment> comments = commentRepository.getAllByActiveTrue();

        if (CollectionUtils.isEmpty(comments)) {
            throw new EntityNotFoundException("There are no comments.");
        }
        return comments;
    }

    @Override
    public Comment getCommentById(Integer id) {
        Comment comment = commentRepository.getByIDAndActiveTrue(id);
        if (comment == null) {
            throw new EntityNotFoundException("There is no comment with that id.");
        }
        return comment;
    }

    @Override
    public List<Comment> getAllCommentsByPost(Post post) {
        List<Comment> allCommentsPost = commentRepository.getAllByPostAndActiveTrue(post);
        List<Comment> activeComments = new ArrayList<>();

        for (Comment comment: allCommentsPost) {
            if (comment.getUser().getActive()){
                activeComments.add(comment);
            }
        }

        if (CollectionUtils.isEmpty(activeComments)){
            throw new EntityNotFoundException("There is no comments.");
        }

        return activeComments;
    }

    @Override
    public Comment createComment(String commentDescription, User loginUser, Post postForCommenting) {

        if (loginUser == null){
            throw new NotAuthorizedException("You are not authorized.");
        }

        if (postForCommenting == null){
            throw new EntityNotFoundException("There is no comment for commenting.");
        }

        Comment comment = new Comment();

        LocalDateTime currentDate = LocalDateTime.now();
        Instant instant = currentDate.atZone(ZoneId.systemDefault()).toInstant();
        comment.setContent(commentDescription);
        comment.setDate(Date.from(instant));
        comment.setActive(true);
        comment.setPost(postForCommenting);
        comment.setUser(loginUser);

        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public void deleteComment(Integer id) {
        Comment forDelete = commentRepository.getByIDAndActiveTrue(id);

        if (forDelete == null) {
            throw new EntityNotFoundException("There is no entity for delete");
        }
        forDelete.setActive(false);
        commentRepository.saveAndFlush(forDelete);
    }

}
