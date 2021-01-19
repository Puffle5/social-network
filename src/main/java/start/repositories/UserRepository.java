package start.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import start.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> getAllByActiveTrue();

    User getByUsernameAndActiveTrue(String name);

    User getUserByUsername(String name);

    User getByIDAndActiveTrue(Integer id);

}
