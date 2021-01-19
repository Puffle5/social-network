package start.services.contracts;

import start.entities.Relationship;
import start.entities.User;

import java.util.List;

public interface RelationshipService {

    List<User> getAllFriends(String user);

    Relationship sendFriendRequest(String name);

    Relationship checkFriendRequest(String name);

    void declineFriendRequest(String name);

    User acceptFriendRequest(String name);

    List<User> getAllRequestsFromUsers();

}
