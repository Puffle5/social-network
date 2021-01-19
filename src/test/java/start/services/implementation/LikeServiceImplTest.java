package start.services.implementation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import start.entities.Like;
import start.entities.Post;
import start.entities.User;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.repositories.LikeRepository;
import start.repositories.PostRepository;
import start.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class LikeServiceImplTest {

    private List<Like> likes;

    @Mock
    private PostRepository postRepository;


    @Mock
    private UserRepository userRepository;


    @Mock
    private LikeRepository mockLikeRepository;

    @InjectMocks
    private LikeServiceImpl likeServiceClassUnderTest;

    @Before
    public void setUp() {
        likes = new ArrayList<>();
        likes.add(new Like(1, true));
        likes.add(new Like(2, true));
    }

    @Test
    public void testGetALLLikes() {
        when(mockLikeRepository.getAllByActiveTrue()).thenReturn(likes);

        final List<Like> result = likeServiceClassUnderTest.getALLLikes();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetLikeById() {

        final Integer id = 0;

        when(mockLikeRepository.getLikeByIDAndActiveTrue(0)).thenReturn(new Like(2, true));

        final Like result = likeServiceClassUnderTest.getLikeById(id);

        assertEquals(true, result.getActive());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetByIdShouldThrowExceptionWhenPutInvalidParameter() {
        likeServiceClassUnderTest.getLikeById(5);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAllCommentsShouldThrowException() {

        List<Like> empty = new ArrayList<>();
        when(mockLikeRepository.getAllByActiveTrue()).thenReturn(empty);

        likeServiceClassUnderTest.getALLLikes();
    }

    @Test
    public void testDeleteLikeById() {

        final Integer id = 0;

        when(mockLikeRepository.getLikeByIDAndActiveTrue(0)).thenReturn(new Like(0, true));

        likeServiceClassUnderTest.deleteLikeById(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteLikeByIdShouldThrowException() {

        final Integer id = 0;

        when(mockLikeRepository.getLikeByIDAndActiveTrue(0)).thenReturn(null);

        likeServiceClassUnderTest.deleteLikeById(id);
    }

    @Test
    public void testCheckIsLiked() {

        final Integer id = 0;
        when(postRepository.getByIDAndActiveTrue(id)).thenReturn(new Post());
        when(mockLikeRepository.getLikeByUserAndPost(any(User.class), any(Post.class))).thenReturn(new Like(0, true));

        Like like = likeServiceClassUnderTest.checkIsLike(new User(), id);
        assertEquals(true, like.getActive());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCheckIsLikedShouldThrowException() {
        final Integer id = 0;
        likeServiceClassUnderTest.checkIsLike(new User(), id);
    }

    @Test
    public void testGetAllLikesByPosts() {
        final Integer id = 0;
        when(postRepository.getByIDAndActiveTrue(id)).thenReturn(new Post());
        when(mockLikeRepository.getAllByPost(any(Post.class))).thenReturn(likes);

        List<Like> likes = likeServiceClassUnderTest.getAllLikesByPost(id);
        assertEquals(2, likes.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAllLikesByPostsShouldThrowException() {
        final Integer id = 0;
        when(postRepository.getByIDAndActiveTrue(id)).thenReturn(new Post());
        when(mockLikeRepository.getAllByPost(any(Post.class))).thenReturn(new ArrayList<>());

        likeServiceClassUnderTest.getAllLikesByPost(id);

    }

    @Test(expected = EntityNotFoundException.class)
    public void testLikePosts() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(postRepository.getByIDAndActiveTrue(0)).thenReturn(new Post());
        when(userRepository.getByUsernameAndActiveTrue(username)).thenReturn(user);

        when(mockLikeRepository.getLikeByUserAndPost(any(User.class), any(Post.class))).thenReturn(new Like());

        likeServiceClassUnderTest.likePosts(0);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testLikePostsShouldThrowExceptionWhenLoginUserIsNull() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        likeServiceClassUnderTest.likePosts(0);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testLikePostsShouldThrowExceptionWhenPostIsNull() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(userRepository.getByUsernameAndActiveTrue(username)).thenReturn(user);

        likeServiceClassUnderTest.likePosts(0);
    }


    @Test
    public void testLikePostsCreateLike() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(postRepository.getByIDAndActiveTrue(0)).thenReturn(new Post());
        when(userRepository.getByUsernameAndActiveTrue(username)).thenReturn(user);

        Like newLike = new Like(0,true);
        Mockito.when(mockLikeRepository.saveAndFlush(any(Like.class))).thenReturn(newLike);

        Like like = likeServiceClassUnderTest.likePosts(0);

        assertEquals(true,like.getActive());
    }


}
