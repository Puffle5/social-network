package start.services.implementation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import start.entities.Relationship;
import start.entities.User;
import start.entities.enumerators.RelationshipStatus;
import start.exceptions.EntityNotFoundException;
import start.exceptions.ExistingEntityException;
import start.exceptions.NotAuthorizedException;
import start.repositories.RelationshipRepository;
import start.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RelationshipServiceImplTest {

    @Mock
    private RelationshipRepository mockRelationshipRepository;
    @Mock
    private UserRepository mockUserRepository;

    private RelationshipServiceImpl relationshipServiceImplUnderTest;

    private List<Relationship> relationships;

    @Before
    public void setUp() {

        relationships = new ArrayList<>();
        User user = new User("username", "password", true);
        relationships.add(new Relationship(RelationshipStatus.FRIENDS, user, user));
        relationships.add(new Relationship(RelationshipStatus.FRIENDS, user, user));
        relationships.add(new Relationship(RelationshipStatus.FRIENDS, user, user));
        relationships.add(new Relationship(RelationshipStatus.FRIENDS, user, user));
        relationshipServiceImplUnderTest = new RelationshipServiceImpl(mockRelationshipRepository, mockUserRepository);
    }

    @Test
    public void testGetAllFriends() {
        // Setup

        final String username = "username";
        User user = new User("username", "password", true);

        when(mockRelationshipRepository.getAllBySecondUserAndStatus(user, RelationshipStatus.FRIENDS)).thenReturn(relationships);
        when(mockRelationshipRepository.getAllByFirstUserAndStatus(user, RelationshipStatus.FRIENDS)).thenReturn(relationships);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Run the test
        final List<User> result = relationshipServiceImplUnderTest.getAllFriends(username);

        // Verify the results
        assertEquals(8, result.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAllFriendsShouldThrowException() {
        // Setup

        final String username = "username";
        User user = new User("username", "password", true);

        when(mockRelationshipRepository.getAllBySecondUserAndStatus(user, RelationshipStatus.FRIENDS)).thenReturn(new ArrayList<>());
        when(mockRelationshipRepository.getAllByFirstUserAndStatus(user, RelationshipStatus.FRIENDS)).thenReturn(new ArrayList<>());

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Run the test
        final List<User> result = relationshipServiceImplUnderTest.getAllFriends(username);

    }


    @Test
    public void testSendFriendRequest() {

        final String username = "username";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        User user = new User("username", "password", true);

        // Setup
        when(mockRelationshipRepository.findByFirstUserAndAndSecondUserAndStatus(user, user, RelationshipStatus.PENDING)).thenReturn(new Relationship(RelationshipStatus.PENDING,user,user));

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Run the test
        relationshipServiceImplUnderTest.sendFriendRequest(username);
    }

    @Test
    public void testSendFriendRequestFromOtherPerson() {

        final String username = "username";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        User user = new User("username", "password", true);


        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Run the test
        relationshipServiceImplUnderTest.sendFriendRequest(username);
    }



    @Test(expected = ExistingEntityException.class)
    public void testSendFriendRequestFromOtherPersonSecondVersion() {

        final String username = "username";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        User user = new User("username", "password", true);

        when(mockRelationshipRepository.findByFirstUserAndAndSecondUserAndStatus(user, user, RelationshipStatus.PENDING)).thenReturn(null ,new Relationship(RelationshipStatus.PENDING,user,user));

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Run the test
        relationshipServiceImplUnderTest.sendFriendRequest(username);

    }

    @Test
    public void testCheckFriendRequest() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Setup
        when(mockRelationshipRepository.findByFirstUserAndAndSecondUserAndStatus(user, user, RelationshipStatus.PENDING)).thenReturn(new Relationship(RelationshipStatus.PENDING,user,user));

        relationshipServiceImplUnderTest.checkFriendRequest(username);
    }

    @Test
    public void testCheckFriendRequestFromOtherPerson() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Setup
        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);

        relationshipServiceImplUnderTest.checkFriendRequest(username);

    }


    @Test(expected = NotAuthorizedException.class)
    public void testCheckFriendRequestShouldThrowException() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        final String username = "username";
        User user = new User("username", "password", true);

        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);
        relationshipServiceImplUnderTest.checkFriendRequest(username);

    }



    @Test
    public void testDeclineFriendRequest() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Setup

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(mockRelationshipRepository.findByFirstUserAndAndSecondUserAndStatus(user, user, RelationshipStatus.PENDING)).thenReturn(new Relationship(RelationshipStatus.PENDING, user, user));
        when(mockRelationshipRepository.saveAndFlush(any(Relationship.class))).thenReturn(new Relationship());

        // Run the test
        relationshipServiceImplUnderTest.declineFriendRequest(username);

        // Verify the results
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeclineFriendRequestShouldThrowException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);

        relationshipServiceImplUnderTest.declineFriendRequest(username);

    }


    @Test
    public void testAcceptFriendRequest() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");
        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);

        // Setup

        when(mockRelationshipRepository.findByFirstUserAndAndSecondUserAndStatus(user, user, RelationshipStatus.PENDING)).thenReturn(new Relationship(RelationshipStatus.PENDING, user, user));

        when(mockRelationshipRepository.saveAndFlush(any(Relationship.class))).thenReturn(new Relationship());

        // Run the test
        final User result = relationshipServiceImplUnderTest.acceptFriendRequest(username);

        // Verify the results
        assertEquals("username", result.getUsername());
    }

    @Test
    public void testGetAllRequestsFromUsers() {

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);


        // Setup
        when(mockRelationshipRepository.getAllBySecondUserAndStatus(user, RelationshipStatus.PENDING)).thenReturn(relationships);

        // Run the test
        final List<User> result = relationshipServiceImplUnderTest.getAllRequestsFromUsers();

        // Verify the results
        assertEquals(4, result.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetAllRequestsFromUsersShouldThrowException() {

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("username");

        final String username = "username";
        User user = new User("username", "password", true);

        when(mockUserRepository.getUserByUsername(username)).thenReturn(user);
        when(relationshipServiceImplUnderTest.getUser(username)).thenReturn(user);


        // Setup
        when(mockRelationshipRepository.getAllBySecondUserAndStatus(user, RelationshipStatus.PENDING)).thenReturn(new ArrayList());

        // Run the test
        final List<User> result = relationshipServiceImplUnderTest.getAllRequestsFromUsers();

    }
}
