package browserbot.scheduler;

//@todo name of?
public class Conditions {

    public long millis;

    public boolean isItActiveDay;

    public boolean isActiveTomorrow;

    public boolean isNineteenHoursAfter;

    public boolean isIntervalLeft;

    public boolean isBeginOfInterval;

    public boolean isItWeekend;

    public boolean isItWeekday;

    public boolean isItTimeToChange;

    public boolean isSchedulerActive;

    public Conditions(long millis) {
        this.millis = millis;
    }

    @Override
    public String toString() {
        return "Conditions{" +
                "millis=" + millis +
                ", isItActiveDay=" + isItActiveDay +
                ", isActiveTomorrow=" + isActiveTomorrow +
                ", isNineteenHoursAfter=" + isNineteenHoursAfter +
                ", isIntervalLeft=" + isIntervalLeft +
                ", isBeginOfInterval=" + isBeginOfInterval +
                ", isItWeekend=" + isItWeekend +
                ", isItWeekday=" + isItWeekday +
                ", isItTimeToChange=" + isItTimeToChange +
                ", isSchedulerActive=" + isSchedulerActive +
                '}';
    }
}
