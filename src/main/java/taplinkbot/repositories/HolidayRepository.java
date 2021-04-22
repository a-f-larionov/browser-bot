package taplinkbot.repositories;

import org.springframework.data.repository.CrudRepository;
import taplinkbot.entities.Holiday;

import java.util.List;

public interface HolidayRepository extends CrudRepository<Holiday, Long> {

    List<Holiday> findAll();

    boolean existsByDayAndMonthAndYear(int day, int month, int year);
}
