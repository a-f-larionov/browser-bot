package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.service.HolidayService;
import taplinkbot.service.StateService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Компонент интервальногоо срабатыванияы
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Trigger {

    private final StateService stateService;

    private final HolidayService holidayService;

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

    public boolean allowRun() {

        if (stateService.allowEveryDay()) {
            return true;
        }

        int[] daysMonToThur = {
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.THURSDAY
        };
        // Mon day, Tues day, Wednes day, Thurs day
        if (Arrays.stream(daysMonToThur).anyMatch(d -> d == getDayOfWeek(0))) return true;

        if (getDayOfWeek(0) == Calendar.FRIDAY && (getHours(0) < 19)) return true;

        return false;
    }

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

    public boolean isItHoliDay(long mills) {
        return holidayService.existsByMills(mills);
    }

    private int getDayOfWeek(long mills) {
        Calendar c = Calendar.getInstance();
        if (mills != 0) c.setTimeInMillis(mills);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public boolean isIntervalLeft(long millis) {
        return getElapsedTime(millis) >= (stateService.getManagerInterval() - getIntervalDeviation() * 1.5);
    }

    public long getElapsedTime(long millis) {
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

        /*log.info(
                "mils" + getMillisFromStartDay(millis) + "\t" +
                        "countIntervals: " + countIntervals + "\t" +
                        "interval" + stateService.getManagerInterval() + "\t" +
                        "offset: " + offset + "\t" +
                        "dev" + getIntervalDeviation()
        );*/

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
        public boolean isItHoliday;

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
    public boolean isItTimeToChange() {
        Trigger.Conditions cond = getConditions();

        return cond.isItTimeToChange;
    }

    /**
     * Возвращает состояние условий для текущего момента времени.
     *
     * @return состояние условий срабатывания
     */
    public Conditions getConditions() {
        return getConditions(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Возвращает состояние условий срабатывания для определенного времени
     *
     * @param millis миллисекунды
     * @return состояние условий срабатывания
     */
    public Conditions getConditions(long millis) {

        Conditions cond = new Conditions(millis);

        cond.isItActiveDay = isActiveToDay(millis);
        cond.isActiveTomorrow = isActiveTomorrow(millis);
        cond.isNineteenHoursAfter = getHours(millis) >= 19;
        cond.isIntervalLeft = isIntervalLeft(millis);
        cond.isBeginOfInterval = isBeginOfInterval(millis);
        cond.isItWeekday = isItWeekDay(millis);
        cond.isItWeekend = isItWeekEnd(millis);
        cond.isItHoliday = isItHoliDay(millis);
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

        /*System.out.print("weekday, weekend, holiday, mills "
                + isItWeekDay(mills) + " "
                + isItWeekEnd(mills) + " "
                + isItHoliDay(mills) + " "
                + mills
                + "\r\n"
        );*/

        // Если выходной запрещен

        //if (isItHoliDay(mills) && !stateService.allowHoliDays()) return false;
        // Если будни запрещены
        if (isItWeekDay(mills) && !stateService.allowWeekDays()) return false;
        // Если выходные запрщены
        if (isItWeekEnd(mills) && !stateService.allowWeekEnds()) return false;

        return true;
    }

    public String checkDate(String argument1) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm");
            Date date = sdf.parse(argument1);
            long millis = date.getTime();

            String msg = "";

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            Conditions cond = getConditions(millis);

            msg += "\r\n " + c.get(Calendar.YEAR) + "/" +
                    c.get(Calendar.MONTH) + "/" +
                    c.get(Calendar.DAY_OF_MONTH) + "_" +
                    c.get(Calendar.HOUR_OF_DAY) + ":" +
                    c.get(Calendar.MINUTE) + " ";

            msg += "\r\nдень активен: " + (cond.isItActiveDay ? "y" : "n");

            msg += "\r\nбудний:" + (cond.isItWeekday ? "y" : "n");
            msg += "\r\nвыходной:" + (cond.isItWeekend ? "y" : "n");
            //msg += "\r\nпраздничный:" + (cond.isItHoliday ? "y" : "n");

            msg += "\r\nменть менеджера?:" + (cond.isItTimeToChange ? "y" : "n");


            return msg;

        } catch (Exception e) {
            return "some thing wrong yyyy/MM/dd_HH:mm";
        }
    }
}
