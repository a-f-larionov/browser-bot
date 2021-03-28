package taplinkbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taplinkbot.entities.Holiday;
import taplinkbot.repositories.HolidayRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    public boolean existsByMills(long mills) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mills);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        return holidayRepository.existsByDayAndMonthAndYear(day, month, year);
    }

    public void add(int day, int month, int year, String comment) {
        Holiday holiday;

        holiday = new Holiday();
        holiday.setDay(day);
        holiday.setMonth(month);
        holiday.setYear(year);
        holiday.setComment(comment);

        holidayRepository.save(holiday);
    }

    public String getInfoText() {
        String msg;
        List<Holiday> holidayList;

        holidayList = holidayRepository.findAll();
        msg = "";

        for (Holiday holiday : holidayList) {
            msg += "id:" + holiday.getId() +
                    " - " + holiday.getDay() + "." + (holiday.getMonth() + 1) + "." + holiday.getYear()
                    + "\r\n";
        }
        return msg;
    }

    public void remove(int id) {
        holidayRepository.deleteById(id);
    }
}
