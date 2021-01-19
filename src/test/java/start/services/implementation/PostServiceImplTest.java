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
import start.entities.Post;
import start.entities.User;
import start.entities.enumerators.PostStatus;
import start.entities.models.PostBindingModel;
import start.exceptions.EntityNotFoundException;
import start.repositories.PostRepository;
import start.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {


    private List<Post> posts;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private PostServiceImpl postServiceClass;

    @Before
    public void setUp() {
        posts = new ArrayList<>();
        posts.add(new Post(1, "sdfsdf", "asdfgsdfg", true));
        posts.add(new Post(2, "sdfsdfd", "dsfgdsf", true));
    }

    @Test
    public void testGetALLPost() {
        when(postRepository.getAllByActiveTrue()).thenReturn(posts);

        final List<Post> result = postServiceClass.getAllPosts();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetPostById() {

        final Integer id = 0;

        when(postRepository.getByIDAndActiveTrue(0)).thenReturn(new Post(2, "dsfs", "asfg", true));

        final Post result = postServiceClass.getPostById(id);

        assertEquals(true, result.getActive());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetByIdShouldThrowExceptionWhenPutInvalidParameter() {
        postServiceClass.getPostById(5);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAllPostsShouldThrowException() {

        List<Post> empty = new ArrayList<>();
        when(postRepository.getAllByActiveTrue()).thenReturn(empty);

        final List<Post> result = postServiceClass.getAllPosts();
    }

    @Test
    public void testCreateAllPostsShouldCreatePost() {

        when(postRepository.saveAndFlush(any(Post.class))).thenReturn(new Post(1, "sdcf", "asdas", true));
        Post newPost = postServiceClass.addPost(new PostBindingModel(1, "sdcf", "asdas"), new User(), "afdf", "PUBLIC");

        assertEquals("sdcf", newPost.getTitle());
    }

    @Test
    public void testDeletePostById() {
        when(postRepository.getByIDAndActiveTrue(1)).thenReturn(new Post());
        postServiceClass.deletePostById(1);
    }

    @Test
    public void testGetAllPublicPosts() {
        User user = new User("", "", true);
        when(postRepository.getAllByUserAndActiveTrue(user)).thenReturn(posts);
        List<User> friends = new ArrayList<>();
        friends.add(user);
        List<Post> friendsPosts = postServiceClass.getAllPublicFriendsPosts(friends, user);
        assertEquals(2, friendsPosts.size());
    }

    @Test
    public void testGetAllPostsByUser() {
        User user = new User("", "", true);
        when(postRepository.getAllByUserAndActiveTrueAndStatusOrderByDateDesc(user, PostStatus.PUBLIC)).thenReturn(posts);

        List<Post> posts = postServiceClass.getAllPostByUser(user);
        assertEquals(2, posts.size());
    }


    @Test
    public void testGetPersonalPosts() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        User user = new User("username", "", true);
        when(userRepository.getByUsernameAndActiveTrue("username")).thenReturn(user);
        when(postRepository.findAllByUserAndActiveTrueOrderByDateDesc(user)).thenReturn(posts);

        List<Post> posts = postServiceClass.getPersonalPosts();
        assertEquals(2, posts.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonalPostsShouldThrowExceptionWhenUserIsNull() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        postServiceClass.getPersonalPosts();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonalPostsShouldThrowException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        User user = new User("username", "", true);
        when(userRepository.getByUsernameAndActiveTrue("username")).thenReturn(user);

        postServiceClass.getPersonalPosts();
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetAllPostsByUserShouldThrowException() {
        User user = new User("", "", true);
        postServiceClass.getAllPostByUser(user);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetAllPublicPostsShouldThrowException() {
        postServiceClass.getAllPublicFriendsPosts(new ArrayList<>(), new User());
    }

    @Test
    public void testSave() {
        Post post = postServiceClass.save(new Post(1, " ", " ", true));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeletePostByIdShouldThrowException() {
        postServiceClass.deletePostById(2);
    }
}
