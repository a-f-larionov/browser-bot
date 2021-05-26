package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
