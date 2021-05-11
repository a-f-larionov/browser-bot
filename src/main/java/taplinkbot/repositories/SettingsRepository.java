package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.Settings;
import taplinkbot.bot.Profile;

public interface SettingsRepository extends CrudRepository<Settings, Integer> {

    Settings findByNameAndProfile(String name, Profile profile);
}
