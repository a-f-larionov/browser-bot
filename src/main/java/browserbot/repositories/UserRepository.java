
package browserbot.repositories;

import org.springframework.data.repository.CrudRepository;
import browserbot.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
