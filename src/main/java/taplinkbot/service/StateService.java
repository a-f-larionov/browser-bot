package taplinkbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import taplinkbot.bot.Profiles;
import taplinkbot.entities.State;
import taplinkbot.repositories.StateRepository;

/**
 * Компонент хранит состояние значений
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StateService {

    private final StateRepository stateRepository;

    private final Profiles profiles;

    public void schedulerSetActive(boolean value) {
        setBooleanValue(State.STATE_SCHEDULER_ACTIVE, value);
    }

    public boolean schedulerIsActive() {
        return getBooleanValue(State.STATE_SCHEDULER_ACTIVE);
    }

    public long getIntervalledLastTimestamp() {
        return getLongValue(State.STATE_LAST_TIMESTAMP);
    }

    public void updateLastTimestamp(long timestamp) {
        saveLongValue(State.STATE_LAST_TIMESTAMP, timestamp);
    }

    private State getStateByName(String name) {
        State state;
        state = stateRepository.findByNameAndBotContext(name, profiles.current());
        if (state == null) {
            state = new State(name, profiles.current());
            stateRepository.save(state);
        }
        return state;
    }

    private boolean getBooleanValue(String name) {
        return getStateByName(name).getBooleanValue();
    }

    private void setBooleanValue(String name, boolean value) {
        State state = getStateByName(name);
        state.setBooleanValue(value);
        stateRepository.save(state);
    }

    private long getLongValue(String name) {
        return getStateByName(name).getLongValue();
    }

    private void saveLongValue(String name, long value) {
        State state = getStateByName(name);
        state.setLongValue(value);
        stateRepository.save(state);
    }

    private int getIntValue(String name) {
        return getStateByName(name).getIntValue();
    }

    private void saveIntValue(String name, int value) {
        State state = getStateByName(name);
        state.setIntValue(value);
        stateRepository.save(state);
    }

    public int getManagerIndex() {
        return getIntValue(State.STATE_LAST_MANAGER_INDEX);
    }

    public void setManagerIndex(int lastIndex) {
        saveIntValue(State.STATE_LAST_MANAGER_INDEX, lastIndex);
    }

    /**
     * Интервал в миллисекундах
     *
     * @return
     */
    public long getManagerInterval() {
        long interval = getLongValue(State.STATE_MANAGER_INTERVAL);
        if (interval == 0) interval = 30 * 60 * 1000;
        return interval;
    }

    public void setIsEveryDay(boolean b) {
        setBooleanValue(State.STATE_ALLOW_EVERY_DAY, b);
    }

    public void setAllowWeekDays(boolean b) {
        setBooleanValue(State.STATE_ALLOW_WEEKDAYS, b);
    }

    public void setAllowWeekEnds(boolean b) {
        setBooleanValue(State.STATE_ALLOW_WEEKENDS, b);
    }

    public void setAllowHolidays(boolean b) {
        setBooleanValue(State.STATE_ALLOW_HOLIDAYS, b);
    }

    public boolean allowEveryDay() {
        return getBooleanValue(State.STATE_ALLOW_EVERY_DAY);
    }

    public boolean allowWeekDays() {
        return getBooleanValue(State.STATE_ALLOW_WEEKDAYS);
    }

    public boolean allowWeekEnds() {
        return getBooleanValue(State.STATE_ALLOW_WEEKENDS);
    }

    public boolean allowHoliDays() {
        return getBooleanValue(State.STATE_ALLOW_HOLIDAYS);
    }
}
