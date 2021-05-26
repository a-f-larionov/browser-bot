package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
