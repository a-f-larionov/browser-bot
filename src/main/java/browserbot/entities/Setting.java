
package browserbot.entities;

import browserbot.bots.taplink.Profile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Setting {

    /**
     * Активно ли расписание
     */
    final public static String SETTING_SCHEDULER_ACTIVE = "schedulerActive";

    /**
     * Последние время срабатывания
     */
    final public static String SETTING_LAST_TIMESTAMP = "lastTimeStamp";

    /**
     * Id менеджера который будет сработан, для ротации
     */
    final public static String SETTING_MANAGER_ID = "lastManagerId";

    /**
     * Интервал срабатывания
     */
    final public static String SETTING_MANAGER_INTERVAL = "managerInterval";

    /**
     * Работать ли в будние дни
     */
    final public static String SETTING_ALLOW_WEEKDAYS = "allowWeekdays";

    /**
     * Работать ли в выходные дни
     */
    final public static String SETTING_ALLOW_WEEKENDS = "allowWeekends";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Название хранимого значения.
     */
    private String name;

    /**
     * Значения int в бд.
     */
    private int intValue;

    /**
     * Значения long в бд.
     */
    private long longValue;

    /**
     * Значение boolean в бд.
     */
    private boolean booleanValue;

    /**
     * Профиль к которому привязано
     */
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

    public long getId() {
        return id;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}