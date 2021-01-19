package start.services.implementation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import start.entities.User;
import start.entities.models.UserBindingModel;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private List<User> users;

    private User user;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userServiceClassUnderTest;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        users.add(new User("username", "password", false));
        users.add(new User("username1", "password1", false));

        user = new User();
    }

    @Test
    public void testGetAllUsers() {

        when(mockUserRepository.getAllByActiveTrue()).thenReturn(users);
        when(mockUserRepository.getByUsernameAndActiveTrue("bam")).thenReturn(new User());

        final List<User> result = userServiceClassUnderTest.getAllUsers("bam");
        assertEquals(2, result.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAllUsersShouldThrowException() {
        List<User> userss = new ArrayList<>();
        when(mockUserRepository.getByUsernameAndActiveTrue("bam")).thenReturn(new User());

        when(mockUserRepository.getAllByActiveTrue()).thenReturn(userss);
        final List<User> result = userServiceClassUnderTest.getAllUsers("bam");
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGetAllUsersShouldThrowExceptionWhenUserIsNull() {
        userServiceClassUnderTest.getAllUsers("bam");
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetAllUsersNameShouldThrowException() {
        List<User> userss = new ArrayList<>();
        when(mockUserRepository.getAllByActiveTrue()).thenReturn(userss);
        final List<String> result = userServiceClassUnderTest.getAllUsersName();
    }

    @Test
    public void testGetAllUsersName() {
        when(mockUserRepository.getAllByActiveTrue()).thenReturn(users);
        final List<String> result = userServiceClassUnderTest.getAllUsersName();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetUserByName() {
        String name = "username";
        when(mockUserRepository.getByUsernameAndActiveTrue("name")).thenReturn(new User("username", "password", false));
        final User result = userServiceClassUnderTest.getUserByName("name");
        assertEquals(name, result.getUsername());
    }

    @Test
    public void testLoginUser() {
        String name = "username";
        when(mockUserRepository.getByUsernameAndActiveTrue("name")).thenReturn(new User("username", "password", false));
        final User result = userServiceClassUnderTest.loginUser("name");
        assertEquals(name, result.getUsername());
    }

    @Test(expected = NotAuthorizedException.class)
    public void testLoginUserShouldThrowException() {
        String name = "username";
        final User result = userServiceClassUnderTest.loginUser("name");
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetUserByNameShouldThrowExceptionWhenInputIsNotValid() {
        final User result = userServiceClassUnderTest.getUserByName("name");
    }

    @Test
    public void testGetUnderTheHoodUser() {

        final String name = "name";
        when(mockUserRepository.getUserByUsername("name")).thenReturn(new User("username", "password", false));

        final User result = userServiceClassUnderTest.getUnderTheHoodUser(name);

        assertEquals(false, result.getActive());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUnderTheHoodUserShouldThrowExceptionWhenInputIsNotValid() {
        final String name = "name";
        final User result = userServiceClassUnderTest.getUnderTheHoodUser(name);
    }


    @Test
    public void testGetUserById() {

        final Integer id = 0;
        when(mockUserRepository.getByIDAndActiveTrue(0)).thenReturn(new User("username", "password", false));

        final User result = userServiceClassUnderTest.getUserById(id);

        assertEquals(false, result.getActive());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserByIdShouldThrowExceptionWhenInputIsNotValid() {

        final User result = userServiceClassUnderTest.getUserById(0);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testGetAllUserEmailsShouldThrowException() {

        final List<String> result = userServiceClassUnderTest.getAllUserEmails();
    }


    @Test
    public void testGetAllUserEmails() {

        when(mockUserRepository.getAllByActiveTrue()).thenReturn(users);
        final List<String> result = userServiceClassUnderTest.getAllUserEmails();

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteUser() {

        final String username = "username";
        when(mockUserRepository.getByUsernameAndActiveTrue("username")).thenReturn(user);
        when(mockUserRepository.saveAndFlush(any(User.class))).thenReturn(new User());

        userServiceClassUnderTest.deleteUser(username);
    }

    @Test
    public void testUpdateInfo() {
        when(mockUserRepository.saveAndFlush(any(User.class))).thenReturn(new User());

        userServiceClassUnderTest.updateInfoUser(new UserBindingModel(), new User());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeleteUserShouldThrowExceptionWhenInputIsNotValid() {

        final String username = "username";
        userServiceClassUnderTest.deleteUser(username);
    }


    @Test
    public void testSave() {

        final User crr = new User("username", "password", false);
        when(mockUserRepository.saveAndFlush(any(User.class))).thenReturn(user);

        userServiceClassUnderTest.save(crr);
    }
}
