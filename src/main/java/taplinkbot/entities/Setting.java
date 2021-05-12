package taplinkbot.entities;

import taplinkbot.bot.Profile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Setting {

    final public static String SETTING_SCHEDULER_ACTIVE = "schedulerActive";

    final public static String SETTING_LAST_TIMESTAMP = "lastTimeStamp";

    final public static String SETTING_LAST_MANAGER_INDEX = "lastManagerIndex";

    final public static String SETTING_MANAGER_INTERVAL = "managerInterval";

    final public static String SETTING_ALLOW_EVERY_DAY = "isEveryDay";

    final public static String SETTING_ALLOW_WEEKDAYS = "allowWeekdays";

    final public static String SETTING_ALLOW_WEEKENDS = "allowWeekends";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int intValue;

    private long longValue;

    private boolean booleanValue;

    private Profile profile;

    public Setting(String name, Profile profile) {
        this.name = name;
        this.profile = profile;
    }

    public Setting() {

    }

    public String getName() {
        return name;
    }

    public Setting setName(String name) {
        this.name = name;
        return this;
    }

    public int getIntValue() {
        return intValue;
    }

    public Setting setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public long getId() {
        return id;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public Setting setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
        return this;
    }
}
