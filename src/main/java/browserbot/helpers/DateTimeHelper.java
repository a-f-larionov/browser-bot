package browserbot.helpers;

import java.util.Arrays;
import java.util.Calendar;

public class DateTimeHelper {

    private static final int[] weekdays = {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY
    };

    private static final int[] weekends = {
            Calendar.SUNDAY,
            Calendar.SATURDAY
    };

    public static int getHours(long millis) {
        Calendar c = Calendar.getInstance();
        if (millis != 0) c.setTimeInMillis(millis);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isItWeekDay(long mills) {
        int dayOfWeek = getDayOfWeek(mills);
        return Arrays.stream(weekdays).anyMatch(v -> v == dayOfWeek);
    }

    public static boolean isItWeekEnd(long mills) {
        int dayOfWeek = getDayOfWeek(mills);
        return Arrays.stream(weekends).anyMatch(v -> v == dayOfWeek);
    }

    public static int getDayOfWeek(long mills) {
        Calendar c = Calendar.getInstance();
        if (mills != 0) c.setTimeInMillis(mills);
        return c.get(Calendar.DAY_OF_WEEK);
    }
}
