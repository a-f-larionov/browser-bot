//FIN
package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.Manager;

import java.util.List;

public interface ManagerRepository extends CrudRepository<Manager, Long> {

    @Override
    List<Manager> findAll();
}
