package start.services.contracts;

import start.entities.User;
import start.entities.models.UserBindingModel;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(String user);

    List<String> getAllUsersName();

    User getUserByName(String name);

    User getUnderTheHoodUser(String name);

    User getUserById(Integer id);

    List<String> getAllUserEmails();

    User updateInfoUser(UserBindingModel userBindingModel, User user);

    void deleteUser(String user);

    void save(User crr);

    User loginUser(String username);
}
