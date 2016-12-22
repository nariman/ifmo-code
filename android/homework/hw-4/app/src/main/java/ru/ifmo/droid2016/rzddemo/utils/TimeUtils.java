package ru.ifmo.droid2016.rzddemo.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static ru.ifmo.droid2016.rzddemo.Constants.TAG;

public final class TimeUtils {

    /**
     * Московский часовой пояс.
     */
    @NonNull
    public static TimeZone getMskTimeZone() {
        if (mskTimeZone == null) {
            try {
                mskTimeZone = TimeZone.getTimeZone("Europe/Moscow");
            } catch (Exception e) {
                Log.e(TAG, "Failed to get MSK time zone: " + e, e);
            }
            if (mskTimeZone == null) {
                mskTimeZone = new SimpleTimeZone(3 * 3600 * 1000, "Europe/Moscow");
            }
        }
        return mskTimeZone;
    }

    public static Calendar getTomorrow0(TimeZone timeZone) {
        Calendar now = Calendar.getInstance(TimeUtils.getMskTimeZone());
        now.setTimeInMillis(System.currentTimeMillis());
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        now.add(Calendar.DAY_OF_MONTH, 1);
        return now;
    }

    public static Calendar getCurrentTime(TimeZone timeZone) {
        Calendar now = Calendar.getInstance(TimeUtils.getMskTimeZone());
        now.setTimeInMillis(System.currentTimeMillis());
        return now;
    }

    public static Calendar getNextDay(Calendar date) {
        Calendar nextDay = Calendar.getInstance(date.getTimeZone());
        nextDay.setTime(date.getTime());
        nextDay.add(Calendar.DAY_OF_MONTH, 1);
        return nextDay;
    }

    private static TimeZone mskTimeZone;

    private TimeUtils() {}
}
