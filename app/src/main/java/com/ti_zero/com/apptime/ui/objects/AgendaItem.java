package com.ti_zero.com.apptime.ui.objects;

import android.opengl.Visibility;
import android.support.annotation.NonNull;

import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anstue on 1/23/18.
 */
public class AgendaItem implements Comparable<AgendaItem> {

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd.MMMM yyyy HH:mm", Locale.US);
    private static SimpleDateFormat sameDayFormat = new SimpleDateFormat("HH:mm", Locale.US);
    private static SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE, ", Locale.US);
    private static SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.US);
    private long uuid;
    private String groupNames;
    private String accountName;
    private Calendar start;
    private Calendar end;
    private boolean open = true;

    public AgendaItem(long uuid, String groupNames, String accountName, Date start, Date end) {
        this.uuid = uuid;
        this.groupNames = groupNames;
        this.accountName = accountName;
        this.start = Calendar.getInstance();
        this.start.setTime(start);
        this.end = Calendar.getInstance();
        if (end != null) {
            open = false;
            this.end.setTime(end);
        }
    }

    public long getUuid() {
        return uuid;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public String getAccountName() {
        return accountName;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    private long getDuration() {
        if (end != null) {
            return end.getTime().getTime() - start.getTime().getTime();
        } else {
            return new Date().getTime() - start.getTime().getTime();
        }
    }

    public String getPrettyPrintDuration() {
        String duration = DurationPrinter.printDuration(getDuration());
        if (end == null) {
            duration = ">" + duration;
        }
        return duration;
    }

    public String getPrettyPrintStart() {
        if (isOnSameDay()) {
            return sameDayFormat.format(start.getTime()) + " - " + sameDayFormat.format(end.getTime());
        } else {
            return simpleDateFormat.format(start.getTime());
        }
    }

    public String getPrettyPrintEnd() {
        long duration = getDuration();
        if (!open) {
            if (isOnSameDay()) {
                return "";
            } else {
                return simpleDateFormat.format(end.getTime());
            }
        }
        return "Open";
    }

    public String getSimilarityOfStartEnd() {
        String similarities = "";
        Calendar today = Calendar.getInstance();
        if (isOnSameDay()) {
            if (!(isInSameYear(today) && isInSameMonth(today))) {
                similarities += monthFormat.format(start.getTime());
            }
            if (!isInSameYear(today)) {
                similarities += " " + start.get(Calendar.YEAR);
            }
            if (!(isInSameYear(today) && isInSameMonth(today) && isOnSameDay(today))) {
                similarities = weekDayFormat.format(start.getTime()) + String.format(Locale.US, "%2d", start.get(Calendar.DAY_OF_MONTH)) + "." + similarities;
            }
        }
        return similarities;
    }

    private boolean isOnSameDay(Calendar today) {
        return today.get(Calendar.DAY_OF_MONTH) == start.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isInSameMonth(Calendar today) {
        return today.get(Calendar.MONTH) == start.get(Calendar.MONTH);
    }

    private boolean isInSameYear(Calendar today) {
        return today.get(Calendar.YEAR) == start.get(Calendar.YEAR);
    }

    @Override
    public int compareTo(@NonNull AgendaItem o) {
        if (o.getStart().getTime().getTime() < this.getStart().getTime().getTime()) {
            return -1;
        } else if (o.getStart().getTime().getTime() > this.getStart().getTime().getTime()) {
            return 1;
        }
        return 0;
    }

    public boolean isOnSameDay() {
        return isInSameYear() && isInSameMonth() && isSameDayOfMonth();
    }

    private boolean isSameDayOfMonth() {
        return start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isInSameMonth() {
        return start.get(Calendar.MONTH) == end.get(Calendar.MONTH);
    }

    private boolean isInSameYear() {
        return start.get(Calendar.YEAR) == end.get(Calendar.YEAR);
    }
}
