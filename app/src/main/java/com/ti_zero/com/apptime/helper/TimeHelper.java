package com.ti_zero.com.apptime.helper;

import com.ti_zero.com.apptime.data.objects.TimeEntry;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by anstue on 12/30/17.
 */

public class TimeHelper {

    private static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static Date getBeginningOfTheDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getEndOfTheDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static long getTimeWithinBoundries(long start, long end, TimeEntry timeEntry) {
        Date endDate = timeEntry.getEnd();
        if(endDate==null) {
            endDate = new Date();
        }
        long relativeStart = timeEntry.getStart().getTime() - start;
        long relativeEnd = endDate.getTime() - end;
        long sum = relativeStart + relativeEnd;
        if (relativeStart > 0 && relativeEnd < 0) {
            //time_entry completely within day
            return timeEntry.getDuration();
        } else if (relativeStart < 0 && relativeEnd < 0 && endDate.getTime() > start) {
            //time_entry started before today and ends today
            long totalTime = timeEntry.getDuration();
            return totalTime + relativeStart;
        } else if (relativeStart > 0 && relativeEnd > 0 && timeEntry.getStart().getTime() < end) {
            //time_entry started within today and ends on another day
            long totalTime = timeEntry.getDuration();
            return totalTime - relativeEnd;
        } else if (relativeStart < 0 && relativeEnd > 0) {
            return DAY_IN_MILLIS;
        }
        return 0;
    }

}
