
package browserbot.services;

import browserbot.bots.taplink.Profile;
import browserbot.entities.Setting;
import browserbot.repositories.SettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Компонент хранит состояние значений
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Settings {

    private final SettingsRepository settingsRepository;

    public void schedulerSetActive(Profile profile, boolean value) {
        setBooleanValue(profile, Setting.SETTING_SCHEDULER_ACTIVE, value);
    }

    public boolean schedulerIsActive(Profile profile) {
        return getBooleanValue(profile, Setting.SETTING_SCHEDULER_ACTIVE);
    }

    public long getIntervalledLastTimestamp(Profile profile) {
        return getLongValue(profile, Setting.SETTING_LAST_TIMESTAMP);
    }

    public void updateLastTimestamp(Profile profile, long timestamp) {
        saveLongValue(profile, Setting.SETTING_LAST_TIMESTAMP, timestamp);
    }

    private Setting getStateByName(Profile profile, String name) {

        Setting setting;
        setting = settingsRepository.findByNameAndProfile(name, profile);
        if (setting == null) {
            setting = new Setting(name, profile);
            settingsRepository.save(setting);
        }
        return setting;
    }

    private boolean getBooleanValue(Profile profile, String name) {
        return getStateByName(profile, name).getBooleanValue();
    }

    private void setBooleanValue(Profile profile, String name, boolean value) {
        Setting setting = getStateByName(profile, name);
        setting.setBooleanValue(value);
        settingsRepository.save(setting);
    }

    private long getLongValue(Profile profile, String name) {
        return getStateByName(profile, name).getLongValue();
    }

    private void saveLongValue(Profile profile, String name, long value) {
        Setting setting = getStateByName(profile, name);
        setting.setLongValue(value);
        settingsRepository.save(setting);
    }

    private int getIntValue(Profile profile, String name) {
        return getStateByName(profile, name).getIntValue();
    }

    private void saveIntValue(Profile profile, String name, int value) {
        Setting setting = getStateByName(profile, name);
        setting.setIntValue(value);
        settingsRepository.save(setting);
    }

    public Long getManagerId(Profile profile) {
        return getLongValue(profile, Setting.SETTING_MANAGER_ID);
    }

    public void setManagerIndex(Profile profile, Long lastIndex) {
        saveLongValue(profile, Setting.SETTING_MANAGER_ID, lastIndex);
    }

    public void setManagerInterval(Profile profile, long interval) {
        saveLongValue(profile, Setting.SETTING_MANAGER_INTERVAL, interval);
    }

    /**
     * Интервал в миллисекундах
     */
    public long getManagerInterval(Profile profile) {
        long interval = getLongValue(profile, Setting.SETTING_MANAGER_INTERVAL);
        if (interval == 0) interval = 30 * 60 * 1000;
        return interval;
    }

    public void setAllowWeekDays(Profile profile, boolean b) {
        setBooleanValue(profile, Setting.SETTING_ALLOW_WEEKDAYS, b);
    }

    public void setAllowWeekEnds(Profile profile, boolean b) {
        setBooleanValue(profile, Setting.SETTING_ALLOW_WEEKENDS, b);
    }

    public boolean allowWeekDays(Profile profile) {
        return getBooleanValue(profile, Setting.SETTING_ALLOW_WEEKDAYS);
    }

    public boolean allowWeekEnds(Profile profile) {
        return getBooleanValue(profile, Setting.SETTING_ALLOW_WEEKENDS);
    }
}
