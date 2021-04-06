package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.PhoneLogger;

public interface PhoneLoggerRepository extends CrudRepository<PhoneLogger, Long> {

}
