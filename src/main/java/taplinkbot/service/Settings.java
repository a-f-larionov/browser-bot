package taplinkbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profiles;
import taplinkbot.repositories.SettingsRepository;

/**
 * Компонент хранит состояние значений
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Settings {

    private final SettingsRepository settingsRepository;

    private final Profiles profiles;

    public void schedulerSetActive(boolean value) {
        setBooleanValue(taplinkbot.entities.Settings.SETTING_SCHEDULER_ACTIVE, value);
    }

    public boolean schedulerIsActive() {
        return getBooleanValue(taplinkbot.entities.Settings.SETTING_SCHEDULER_ACTIVE);
    }

    public long getIntervalledLastTimestamp() {
        return getLongValue(taplinkbot.entities.Settings.SETTING_LAST_TIMESTAMP);
    }

    public void updateLastTimestamp(long timestamp) {
        saveLongValue(taplinkbot.entities.Settings.SETTING_LAST_TIMESTAMP, timestamp);
    }

    private taplinkbot.entities.Settings getStateByName(String name) {

        taplinkbot.entities.Settings settings;
        settings = settingsRepository.findByNameAndProfile(name, profiles.current());
        if (settings == null) {
            settings = new taplinkbot.entities.Settings(name, profiles.current());
            settingsRepository.save(settings);
        }
        return settings;
    }

    private boolean getBooleanValue(String name) {
        return getStateByName(name).getBooleanValue();
    }

    private void setBooleanValue(String name, boolean value) {
        taplinkbot.entities.Settings settings = getStateByName(name);
        settings.setBooleanValue(value);
        settingsRepository.save(settings);
    }

    private long getLongValue(String name) {
        return getStateByName(name).getLongValue();
    }

    private void saveLongValue(String name, long value) {
        taplinkbot.entities.Settings settings = getStateByName(name);
        settings.setLongValue(value);
        settingsRepository.save(settings);
    }

    private int getIntValue(String name) {
        return getStateByName(name).getIntValue();
    }

    private void saveIntValue(String name, int value) {
        taplinkbot.entities.Settings settings = getStateByName(name);
        settings.setIntValue(value);
        settingsRepository.save(settings);
    }

    public int getManagerIndex() {
        return getIntValue(taplinkbot.entities.Settings.SETTING_LAST_MANAGER_INDEX);
    }

    public void setManagerIndex(int lastIndex) {
        saveIntValue(taplinkbot.entities.Settings.SETTING_LAST_MANAGER_INDEX, lastIndex);
    }

    /**
     * Интервал в миллисекундах
     *
     * @return
     */
    public long getManagerInterval() {
        long interval = getLongValue(taplinkbot.entities.Settings.SETTING_MANAGER_INTERVAL);
        if (interval == 0) interval = 30 * 60 * 1000;
        return interval;
    }

    public void setAllowWeekDays(boolean b) {
        setBooleanValue(taplinkbot.entities.Settings.SETTING_ALLOW_WEEKDAYS, b);
    }

    public void setAllowWeekEnds(boolean b) {
        setBooleanValue(taplinkbot.entities.Settings.SETTING_ALLOW_WEEKENDS, b);
    }

    public boolean allowWeekDays() {
        return getBooleanValue(taplinkbot.entities.Settings.SETTING_ALLOW_WEEKDAYS);
    }

    public boolean allowWeekEnds() {
        return getBooleanValue(taplinkbot.entities.Settings.SETTING_ALLOW_WEEKENDS);
    }
}
