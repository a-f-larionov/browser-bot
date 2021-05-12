package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
import taplinkbot.helpers.DateTimeHelper;
import taplinkbot.service.Settings;

import java.util.Calendar;

/**
 * Определяет, когда срабатывать расписанию с учетом интервала, времени последнего срабатывания.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class Trigger {

    private final Settings settings;

    /**
     * Необходимо ли сейчас срабатывать.
     *
     * @return true - время срабатывать, иначе false
     */
    public boolean isItTimeToChange(Profile profile) {
        return getConditions(profile)
                .isItTimeToChange;
    }

    /**
     * Возвращает состояние условий для текущего момента времени.
     *
     * @return состояние условий срабатывания
     */
    public Conditions getConditions(Profile profile) {
        return getConditions(profile, Calendar.getInstance().getTimeInMillis());
    }

    public void updateLastTime() {
        settings.updateLastTimestamp(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Возвращает состояние условий срабатывания для определенного времени
     *
     * @param millis миллисекунды
     * @return состояние условий срабатывания
     */
    private Conditions getConditions(Profile profile, long millis) {

        Conditions cond = new Conditions(millis);

        cond.isItActiveDay = isActiveToDay(millis);
        cond.isActiveTomorrow = isActiveTomorrow(millis);
        cond.isNineteenHoursAfter = DateTimeHelper.getHours(millis) >= 19;
        cond.isIntervalLeft = isIntervalLeft(millis);
        cond.isBeginOfInterval = isBeginOfInterval(millis);
        cond.isItWeekday = DateTimeHelper.isItWeekDay(millis);
        cond.isItWeekend = DateTimeHelper.isItWeekEnd(millis);
        cond.isSchedulerActive = settings.schedulerIsActive();

        cond.isItTimeToChange = true;

        if (!cond.isSchedulerActive) cond.isActiveTomorrow = false;
        if (!cond.isItActiveDay) cond.isItTimeToChange = false;
        if (!cond.isActiveTomorrow && cond.isNineteenHoursAfter) cond.isItTimeToChange = false;
        if (!cond.isIntervalLeft) cond.isItTimeToChange = false;
        if (!cond.isBeginOfInterval) cond.isItTimeToChange = false;

        return cond;
    }


    private boolean isIntervalLeft(long millis) {
        return getElapsedTime(millis) >= (settings.getManagerInterval() - getIntervalDeviation() * 1.5);
    }

    private long getElapsedTime(long millis) {
        return millis - settings.getIntervalledLastTimestamp();
    }

    /**
     * Возвращает отклонение от интервала
     * Это необходимо чтобы задержка предыдущего тика, не сдвигала все интервалы
     *
     * @return long time deviation
     */
    private long getIntervalDeviation() {
        return settings.getManagerInterval() / 3;
    }

    private boolean isBeginOfInterval(long millis) {

        int countIntervals = (int) (getStartOfTheDayMillis(millis) / settings.getManagerInterval());
        long offset = getStartOfTheDayMillis(millis) - (countIntervals * settings.getManagerInterval());

        return offset <= getIntervalDeviation();
    }

    private long getStartOfTheDayMillis(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return millis - c.getTimeInMillis();
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
     */
    private boolean isItDayActive(long mills) {

        // Если будни запрещены
        if (DateTimeHelper.isItWeekDay(mills) && !settings.allowWeekDays()) return false;

        // Если выходные запрщены
        return !DateTimeHelper.isItWeekEnd(mills) || settings.allowWeekEnds();
    }
}
