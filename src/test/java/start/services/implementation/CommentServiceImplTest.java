package start.services.implementation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import start.entities.Comment;
import start.entities.Post;
import start.entities.User;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {

    private List<Comment> comments;

    @Mock
    private CommentRepository mockCommentRepository;

    @InjectMocks
    private CommentServiceImpl commentServiceClassUnderTest;


    @Before
    public void setUp() {
        comments = new ArrayList<>();
        comments.add(new Comment(1, "asdasd", true));
        comments.add(new Comment(2, "asdasd", true));
        comments.get(0).setUser(new User("", "", true));
        comments.get(1).setUser(new User("", "", true));
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetAllCommentsShouldThrowException() {

        List<Comment> empty = new ArrayList<>();
        when(mockCommentRepository.getAllByActiveTrue()).thenReturn(empty);

        final List<Comment> result = commentServiceClassUnderTest.getAllComments();

    }

    @Test
    public void testGetAllComments() {
        when(mockCommentRepository.getAllByActiveTrue()).thenReturn(comments);

        final List<Comment> result = commentServiceClassUnderTest.getAllComments();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllCommentsByPost() {
        Post post = new Post();
        when(mockCommentRepository.getAllByPostAndActiveTrue(post)).thenReturn(comments);

        final List<Comment> result = commentServiceClassUnderTest.getAllCommentsByPost(post);

        assertEquals(2, result.size());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetAllCommentsByPostShouldThrowException() {
        Post post = new Post();

        commentServiceClassUnderTest.getAllCommentsByPost(post);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetCommentById() {
        commentServiceClassUnderTest.getCommentById(5);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetByIdShouldThrowExceptionWhenPutInvalidParameter() {
        commentServiceClassUnderTest.getCommentById(5);
    }

    @Test
    public void testDeleteCommentById() {
        when(mockCommentRepository.getByIDAndActiveTrue(1)).thenReturn(new Comment());
        commentServiceClassUnderTest.deleteComment(1);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCommentByIdShouldThrowException() {
        commentServiceClassUnderTest.deleteComment(1);
    }

    @Test
    public void testGetByIdShouldReturnCorrectComment() {
        Mockito.when(mockCommentRepository.getByIDAndActiveTrue(1)).thenReturn(new Comment(1, "here", true));

        Mockito.when(commentServiceClassUnderTest.getCommentById(1)).thenReturn(new Comment(1, "here", true));

        Comment comment = commentServiceClassUnderTest.getCommentById(1);
        assertEquals("here", comment.getContent());
    }

    @Test
    public void testCreateComment() {
        Post post = new Post();
        User user = new User();
        commentServiceClassUnderTest.createComment("dadsa", user, post);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testCreateCommentShouldThrowExceptionWhenUserIsNull() {
        Post post = new Post();
        commentServiceClassUnderTest.createComment("dadsa", null, post);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateCommentShouldThrowExceptionWhenPostIsNull() {
        User user = new User();
        commentServiceClassUnderTest.createComment("dadsa", user, null);
    }

}
