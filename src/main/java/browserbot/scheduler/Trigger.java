package browserbot.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import browserbot.bots.taplink.Profile;
import browserbot.helpers.DateTimeHelper;
import browserbot.services.Settings;

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

    public String getNext(Profile profile) {
        Conditions cond;
        long millis = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < 100; i++) {
            cond = getConditions(profile,
                    millis +
                            (i * 1000 * 60)
            );
            if (cond.isItTimeToChange) {
                Calendar calendar;
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis + i * 1000 * 60);
                return calendar.getTime().toString();
            }
        }
        return "n/a";
    }

    public void updateLastTime(Profile profile) {
        settings.updateLastTimestamp(profile, Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Возвращает состояние условий срабатывания для определенного времени
     *
     * @param millis миллисекунды
     * @return состояние условий срабатывания
     */
    private Conditions getConditions(Profile profile, long millis) {

        Conditions cond = new Conditions(millis);

        cond.isItActiveDay = isActiveToDay(profile, millis);
        cond.isActiveTomorrow = isActiveTomorrow(profile, millis);
        cond.isNineteenHoursAfter = DateTimeHelper.getHours(millis) >= 19;
        cond.isIntervalLeft = isIntervalLeft(profile, millis);
        cond.isBeginOfInterval = isBeginOfInterval(profile, millis);
        cond.isItWeekday = DateTimeHelper.isItWeekDay(millis);
        cond.isItWeekend = DateTimeHelper.isItWeekEnd(millis);
        cond.isSchedulerActive = settings.schedulerIsActive(profile);

        cond.isItTimeToChange = cond.isSchedulerActive;
        if (!cond.isItActiveDay) cond.isItTimeToChange = false;
        if (!cond.isActiveTomorrow && cond.isNineteenHoursAfter) cond.isItTimeToChange = false;
        if (!cond.isIntervalLeft) cond.isItTimeToChange = false;
        if (!cond.isBeginOfInterval) cond.isItTimeToChange = false;

        return cond;
    }


    private boolean isIntervalLeft(Profile profile, long millis) {
        return getElapsedTime(profile, millis) >= (settings.getManagerInterval(profile) - getIntervalDeviation(profile) * 1.5);
    }

    private long getElapsedTime(Profile profile, long millis) {
        return millis - settings.getIntervalledLastTimestamp(profile);
    }

    /**
     * Возвращает отклонение от интервала
     * Это необходимо чтобы задержка предыдущего тика, не сдвигала все интервалы
     *
     * @return long time deviation
     */
    private long getIntervalDeviation(Profile profile) {
        return settings.getManagerInterval(profile) / 3;
    }

    private boolean isBeginOfInterval(Profile profile, long millis) {

        int countIntervals = (int) (getStartOfTheDayMillis(millis) / settings.getManagerInterval(profile));
        long offset = getStartOfTheDayMillis(millis) - (countIntervals * settings.getManagerInterval(profile));

        return offset <= getIntervalDeviation(profile);
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

    private boolean isActiveToDay(Profile profile, long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);

        return isItDayActive(profile, c.getTimeInMillis());
    }

    private boolean isActiveTomorrow(Profile profile, long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.add(Calendar.DAY_OF_MONTH, 1);

        return isItDayActive(profile, c.getTimeInMillis());
    }

    /**
     * Выполнять ли действие сегодня
     *
     * @param mills long миллисекунды за какой день запрос
     */
    private boolean isItDayActive(Profile profile, long mills) {

        // Если будни запрещены
        if (DateTimeHelper.isItWeekDay(mills) && !settings.allowWeekDays(profile)) return false;

        // Если выходные запрщены
        return !DateTimeHelper.isItWeekEnd(mills) || settings.allowWeekEnds(profile);
    }
}
