//FIN
package browserbot.repositories;

import org.springframework.data.repository.CrudRepository;
import browserbot.entities.Manager;

import java.util.List;

public interface ManagerRepository extends CrudRepository<Manager, Long> {

    @Override
    List<Manager> findAll();
}
