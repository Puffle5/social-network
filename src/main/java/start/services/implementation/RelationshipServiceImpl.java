package start.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import start.entities.Relationship;
import start.entities.User;
import start.entities.enumerators.RelationshipStatus;
import start.exceptions.EntityNotFoundException;
import start.exceptions.ExistingEntityException;
import start.exceptions.NotAuthorizedException;
import start.find.login.user.FindLoginUser;
import start.repositories.RelationshipRepository;
import start.repositories.UserRepository;
import start.services.contracts.RelationshipService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipService {

    private RelationshipRepository relationshipRepository;
    private UserRepository userRepository;

    @Autowired
    public RelationshipServiceImpl(RelationshipRepository relationshipRepository, UserRepository userRepository) {
        this.relationshipRepository = relationshipRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllFriends(String username) {
        User loginUser = getUser(username);

        List<User> users = new ArrayList<>();
        List<Relationship> relationships =
                relationshipRepository.getAllBySecondUserAndStatus(loginUser, RelationshipStatus.FRIENDS);

        for (Relationship relationship : relationships) {
            if (relationship.getFirstUser().getActive()) {
                users.add(relationship.getFirstUser());
            }
        }

        relationships = relationshipRepository.getAllByFirstUserAndStatus(loginUser, RelationshipStatus.FRIENDS);

        for (Relationship relationship : relationships) {
            if (relationship.getSecondUser().getActive()) {
                users.add(relationship.getSecondUser());
            }
        }

        if (CollectionUtils.isEmpty(users)) {
            throw new EntityNotFoundException("There are no friends");
        }

        return users;
    }


    @Override
    public Relationship sendFriendRequest(String name) {
        User loginUser = getUser(FindLoginUser.findLoginUserName());

        User userReceiveRequest = getUser(name);

        Relationship currRelationship = relationshipRepository.
                findByFirstUserAndAndSecondUserAndStatus(loginUser, userReceiveRequest, RelationshipStatus.PENDING);

        if (currRelationship != null) {
            relationshipRepository.delete(currRelationship);
            return new Relationship();
        }

        Relationship currRelationshipFromOtherPerson = relationshipRepository.
                findByFirstUserAndAndSecondUserAndStatus(userReceiveRequest, loginUser, RelationshipStatus.PENDING);

        if (currRelationshipFromOtherPerson != null) {
            throw new ExistingEntityException("You already have a request.");
        }

        Relationship newRelationship =
                new Relationship(RelationshipStatus.PENDING, loginUser, userReceiveRequest);

        return relationshipRepository.saveAndFlush(newRelationship);
    }

    @Override
    public Relationship checkFriendRequest(String name) {
        User loginUser = getUser(FindLoginUser.findLoginUserName());

        User userReceiveRequest = getUser(name);

        Relationship currRelationship = relationshipRepository.
                findByFirstUserAndAndSecondUserAndStatus(loginUser, userReceiveRequest, RelationshipStatus.PENDING);

        if (currRelationship != null) {
            return currRelationship;
        }

        currRelationship = relationshipRepository.
                findByFirstUserAndAndSecondUserAndStatus(loginUser, userReceiveRequest, RelationshipStatus.FRIENDS);

        if (currRelationship != null) {
            return new Relationship(RelationshipStatus.FRIENDS, null, new User());
        }

        currRelationship = relationshipRepository.
                findByFirstUserAndAndSecondUserAndStatus(userReceiveRequest, loginUser, RelationshipStatus.FRIENDS);


        if (currRelationship != null) {
            return new Relationship(RelationshipStatus.FRIENDS, null, new User());
        }

        return new Relationship();
    }

    @Override
    public void declineFriendRequest(String name) {
        User loginUser = getUser(FindLoginUser.findLoginUserName());

        User userDeclineFriendRequest = getUser(name);

        Relationship currRelationship = getRelationship(loginUser, userDeclineFriendRequest);

        currRelationship.setStatus(RelationshipStatus.NEUTRAL);

        relationshipRepository.saveAndFlush(currRelationship);
    }

    @Override
    public User acceptFriendRequest(String name) {
        User loginUser = getUser(FindLoginUser.findLoginUserName());

        User userAcceptFriendRequest = getUser(name);

        Relationship currRelationship = getRelationship(loginUser, userAcceptFriendRequest);

        currRelationship.setStatus(RelationshipStatus.FRIENDS);

        relationshipRepository.saveAndFlush(currRelationship);
        return userAcceptFriendRequest;
    }

    @Override
    public List<User> getAllRequestsFromUsers() {

        User loginUser = getUser(FindLoginUser.findLoginUserName());


        List<Relationship> relationships =
                relationshipRepository.getAllBySecondUserAndStatus(loginUser, RelationshipStatus.PENDING);

        if (CollectionUtils.isEmpty(relationships)) {
            throw new EntityNotFoundException("There are no friend request.");
        }
        List<User> users = new ArrayList<>();

        for (Relationship relationship : relationships) {
            if (relationship.getFirstUser().getActive()) {
                users.add(relationship.getFirstUser());
            }
        }

        return users;
    }


    private Relationship getRelationship(User loginUser, User otherUser) {
        Relationship currRelationship = relationshipRepository.
                findByFirstUserAndAndSecondUserAndStatus(otherUser, loginUser, RelationshipStatus.PENDING);

        if (currRelationship == null) {
            throw new EntityNotFoundException("You don't have request from that user.");
        }
        return currRelationship;
    }


    public User getUser(String name) {
        User userDeclineFriendRequest = userRepository.getUserByUsername(name);

        if (userDeclineFriendRequest == null) {
            throw new NotAuthorizedException("You are not authorized.");
        }

        return userDeclineFriendRequest;
    }

}
