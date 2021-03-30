package taplinkbot.entities;

import taplinkbot.telegram.BotContext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class State {

    final public static String STATE_SCHEDULER_ACTIVE = "schedulerActive";

    final public static String STATE_LAST_TIMESTAMP = "lastTimeStamp";

    final public static String STATE_LAST_MANAGER_INDEX = "lastManagerIndex";

    final public static String STATE_MANAGER_INTERVAL = "managerInterval";

    final public static String STATE_ALLOW_EVERY_DAY = "isEveryDay";

    final public static String STATE_ALLOW_WEEKDAYS = "allowWeekdays";

    final public static String STATE_ALLOW_WEEKENDS = "allowWeekends";

    final public static String STATE_ALLOW_HOLIDAYS = "allowHolidays";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int intValue;

    private long longValue;

    private boolean booleanValue;

    private BotContext botContext;

    public State(String name, BotContext botContext) {
        this.name = name;
        this.botContext = botContext;
    }

    public State() {

    }

    public String getName() {
        return name;
    }

    public State setName(String name) {
        this.name = name;
        return this;
    }

    public int getIntValue() {
        return intValue;
    }

    public State setIntValue(int intValue) {
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

    public State setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
        return this;
    }
}
