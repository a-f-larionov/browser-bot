package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.State;

public interface StateRepository extends CrudRepository<State, Integer> {

    public State findByName(String lastTimestamp);
}
