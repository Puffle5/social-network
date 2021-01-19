package start.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import start.entities.Relationship;
import start.entities.User;
import start.entities.enumerators.RelationshipStatus;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    List<Relationship> getAllByFirstUserAndStatus(User user, RelationshipStatus status);

    Relationship findByFirstUserAndAndSecondUserAndStatus(User firstUser, User secondUser, RelationshipStatus status);

    List<Relationship> getAllBySecondUserAndStatus(User user, RelationshipStatus status);

    List<Relationship> getAllBySecondUserOrAndFirstUserAndStatus(User firstUser,User secondUser, RelationshipStatus status);
}
