
package browserbot.repositories;

import org.springframework.data.repository.CrudRepository;
import browserbot.entities.PageLoadTime;

public interface URLProfilerRepository extends CrudRepository<PageLoadTime, Long> {

}
