package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.State;
import taplinkbot.bot.BotContext;

public interface StateRepository extends CrudRepository<State, Integer> {

    State findByNameAndBotContext(String name, BotContext botContext);
}
