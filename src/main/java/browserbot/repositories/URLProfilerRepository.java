//FIN
package browserbot.repositories;

import org.springframework.data.repository.CrudRepository;
import browserbot.entities.PageLoads;

public interface URLProfilerRepository extends CrudRepository<PageLoads, Long> {

}
