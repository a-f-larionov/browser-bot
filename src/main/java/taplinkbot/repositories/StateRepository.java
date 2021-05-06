package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.State;
import taplinkbot.bot.Profile;

public interface StateRepository extends CrudRepository<State, Integer> {

    State findByNameAndProfile(String name, Profile profile);
}
