package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.State;
import taplinkbot.bot.Context;

public interface StateRepository extends CrudRepository<State, Integer> {

    State findByNameAndBotContext(String name, Context botContext);
}
