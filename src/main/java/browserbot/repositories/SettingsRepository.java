//FIN
package browserbot.repositories;

import org.springframework.data.repository.CrudRepository;
import browserbot.entities.Setting;
import browserbot.bots.taplink.Profile;

public interface SettingsRepository extends CrudRepository<Setting, Integer> {

    Setting findByNameAndProfile(String name, Profile profile);
}
