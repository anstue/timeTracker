package com.ti_zero.com.apptime.ui.objects;

import android.support.annotation.NonNull;

import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anstue on 1/23/18.
 */
public class AgendaItem implements Comparable<AgendaItem> {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, yyyy-MM-dd HH:mm", Locale.US);
    private long uuid;
    private String groupNames;
    private String accountName;
    private Date start;
    private Date end;

    public AgendaItem(long uuid, String groupNames, String accountName, Date start, Date end) {
        this.uuid = uuid;
        this.groupNames = groupNames;
        this.accountName = accountName;
        this.start = start;
        this.end = end;
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

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    private long getDuration() {
        if(end!=null) {
            return end.getTime() - start.getTime();
        } else {
            return new Date().getTime() - start.getTime();
        }
    }

    public String getPrettyPrintDuration() {
        String duration =  DurationPrinter.printDuration(getDuration());
        if(end==null) {
            duration = ">"+duration;
        }
        return duration;
    }

    public String getPrettyPrintStart() {
        return simpleDateFormat.format(start);
    }

    public String getPrettyPrintEnd() {
        if(end != null) {
            return simpleDateFormat.format(end);
        }
        return "Open";
    }

    @Override
    public int compareTo(@NonNull AgendaItem o) {
        if(o.getStart().getTime()<this.getStart().getTime()) {
            return -1;
        } else if (o.getStart().getTime()>this.getStart().getTime()) {
            return 1;
        }
        return 0;
    }
}
