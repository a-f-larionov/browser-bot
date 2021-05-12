//FIN
package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.Setting;
import taplinkbot.bot.Profile;

public interface SettingsRepository extends CrudRepository<Setting, Integer> {

    Setting findByNameAndProfile(String name, Profile profile);
}
