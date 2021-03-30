package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.State;
import taplinkbot.telegram.BotContext;

public interface StateRepository extends CrudRepository<State, Integer> {

    State findByNameAndBotContext(String name, BotContext botContext);
}
