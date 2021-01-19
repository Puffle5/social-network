package start.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import start.entities.User;
import start.entities.models.UserBindingModel;
import start.exceptions.EntityNotFoundException;
import start.exceptions.NotAuthorizedException;
import start.repositories.UserRepository;
import start.services.contracts.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers(String username) {

        User loginUser = userRepository.getByUsernameAndActiveTrue(username);

        if (loginUser == null) {
            throw new NotAuthorizedException("You are not authorized.");
        }

        List<User> users = userRepository.getAllByActiveTrue();
        if (CollectionUtils.isEmpty(users)) {
            throw new EntityNotFoundException("There are no users.");
        }
        return users;
    }

    @Override
    public List<String> getAllUsersName() {
        List<String> userNames = new ArrayList<>();

        List<User> users = userRepository.getAllByActiveTrue();

        users.forEach(user -> userNames.add(user.getUsername()));

        if (CollectionUtils.isEmpty(userNames)) {
            throw new EntityNotFoundException("No existing names.");
        }
        return userNames;
    }


    @Override
    public User getUserByName(String name) {
        User user = userRepository.getByUsernameAndActiveTrue(name);

        if (user == null) {
            throw new EntityNotFoundException("There is no user with that name.");
        }
        return user;
    }

    @Override
    public User getUnderTheHoodUser(String name) {
        User user = userRepository.getUserByUsername(name);
        if (user == null) {
            throw new EntityNotFoundException("There is no user with that name.");
        }
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        User user = userRepository.getByIDAndActiveTrue(id);

        if (user == null) {
            throw new EntityNotFoundException("There is no user with that id.");
        }
        return user;
    }

    @Override
    public List<String> getAllUserEmails() {
        List<String> userEmail = new ArrayList<>();

        for (User user : userRepository.getAllByActiveTrue()) {
            userEmail.add(user.getEmail());
        }

        if (userEmail.isEmpty()) {
            throw new EntityNotFoundException("No existing emails.");
        }
        return userEmail;
    }

    @Override
    public User updateInfoUser(UserBindingModel userBindingModel, User user) {
        user.setEmail(userBindingModel.getEmail());
        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public void deleteUser(String username) {
        User userToDelete = userRepository.getByUsernameAndActiveTrue(username);

        if (userToDelete == null) {
            throw new EntityNotFoundException("There is no user.");
        }

        userToDelete.setActive(false);
        userToDelete.setUsername(userToDelete.getUsername() + "deleteUser");
        save(userToDelete);
    }

    @Override
    public void save(User userToSave) {
        userRepository.saveAndFlush(userToSave);
    }

    @Override
    public User loginUser(String username) {
        User loginUser = userRepository.getByUsernameAndActiveTrue(username);
        if (loginUser == null) {
            throw new NotAuthorizedException("You are not authorized.");
        }
        return loginUser;
    }
}
