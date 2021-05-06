package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
import taplinkbot.service.StateService;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Компонент интервальногоо срабатывания
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Trigger {

    private final StateService stateService;

    final int[] weekdays = {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY
    };

    final int[] weekends = {
            Calendar.SUNDAY,
            Calendar.SATURDAY
    };

    private int getHours(long millis) {
        Calendar c = Calendar.getInstance();
        if (millis != 0) c.setTimeInMillis(millis);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    private boolean isItWeekDay(long mills) {
        int dayOfWeek = getDayOfWeek(mills);
        return Arrays.stream(weekdays).anyMatch(v -> v == dayOfWeek);
    }

    private boolean isItWeekEnd(long mills) {
        int dayOfWeek = getDayOfWeek(mills);
        return Arrays.stream(weekends).anyMatch(v -> v == dayOfWeek);
    }

    private int getDayOfWeek(long mills) {
        Calendar c = Calendar.getInstance();
        if (mills != 0) c.setTimeInMillis(mills);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public boolean isIntervalLeft(long millis) {

        System.out.println("1   " + stateService.getManagerInterval());

        return getElapsedTime(millis) >= (stateService.getManagerInterval() - getIntervalDeviation() * 1.5);
    }

    public long getElapsedTime(long millis) {

        System.out.println("2   " + stateService.getIntervalledLastTimestamp());

        return millis - stateService.getIntervalledLastTimestamp();
    }

    /**
     * Возвращает отклонение от интервала
     * Это необходимо чтобы задержка предыдущего тика, не сдвигала все интервалы
     *
     * @return long time deviation
     */
    private long getIntervalDeviation() {
        return stateService.getManagerInterval() / 3;
    }

    public boolean isBeginOfInterval(long millis) {

        int countIntervals = (int) (getMillisFromStartDay(millis) / stateService.getManagerInterval());
        long offset = getMillisFromStartDay(millis) - (countIntervals * stateService.getManagerInterval());

        return offset <= getIntervalDeviation();
    }

    private long getMillisFromStartDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return millis - c.getTimeInMillis();
    }

    public void updateLastTime() {
        stateService.updateLastTimestamp(Calendar.getInstance().getTimeInMillis());
    }

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
    }


    /**
     * Необходимо ли сейчас срабатывать.
     *
     * @return true - время срабатывать, иначе false
     */
    public boolean isItTimeToChange(Profile profile) {
        Trigger.Conditions cond = getConditions(profile);

        return cond.isItTimeToChange;
    }

    /**
     * Возвращает состояние условий для текущего момента времени.
     *
     * @return состояние условий срабатывания
     */
    public Conditions getConditions(Profile profile) {
        return getConditions(profile, Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Возвращает состояние условий срабатывания для определенного времени
     *
     * @param millis миллисекунды
     * @return состояние условий срабатывания
     */
    public Conditions getConditions(Profile profile, long millis) {

        Conditions cond = new Conditions(millis);

        cond.isItActiveDay = isActiveToDay(millis);
        cond.isActiveTomorrow = isActiveTomorrow(millis);
        cond.isNineteenHoursAfter = getHours(millis) >= 19;
        cond.isIntervalLeft = isIntervalLeft(millis);
        cond.isBeginOfInterval = isBeginOfInterval(millis);
        cond.isItWeekday = isItWeekDay(millis);
        cond.isItWeekend = isItWeekEnd(millis);
        cond.isSchedulerActive = stateService.schedulerIsActive();

        cond.isItTimeToChange = true;

        if (!cond.isSchedulerActive) cond.isActiveTomorrow = false;
        if (!cond.isItActiveDay) cond.isItTimeToChange = false;
        if (!cond.isActiveTomorrow && cond.isNineteenHoursAfter) cond.isItTimeToChange = false;
        if (!cond.isIntervalLeft) cond.isItTimeToChange = false;
        if (!cond.isBeginOfInterval) cond.isItTimeToChange = false;

        return cond;
    }

    private boolean isActiveToDay(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);

        return isItDayActive(c.getTimeInMillis());
    }

    private boolean isActiveTomorrow(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_MONTH, 1);

        return isItDayActive(c.getTimeInMillis());
    }

    /**
     * Выполнять ли действие сегодня
     *
     * @param mills long миллисекунды за какой день запрос
     * @return
     */
    private boolean isItDayActive(long mills) {

        // Если будни запрещены
        if (isItWeekDay(mills) && !stateService.allowWeekDays()) return false;

        // Если выходные запрщены
        return !isItWeekEnd(mills) || stateService.allowWeekEnds();
    }
}
