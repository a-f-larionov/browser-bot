//FIN
package browserbot.repositories;

import browserbot.entities.Manager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ManagerRepository extends CrudRepository<Manager, Long> {

    @Override
    List<Manager> findAll();

    @Query(value = "SELECT id FROM manager WHERE id > ?1 ORDER BY id LIMIT 1", nativeQuery = true)
    Long getAfterId(long managerId);

    @Query(value = "SELECT MIN(id) FROM manager", nativeQuery = true)
    long getFirstId();
}
