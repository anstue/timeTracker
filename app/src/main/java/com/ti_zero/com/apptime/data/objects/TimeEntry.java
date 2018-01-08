package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by uni on 12/22/17.
 */

public class TimeEntry extends ObservableWithUUID implements Serializable {

    private Date start;
    private Date end = null;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, yyyy-MM-dd HH:mm", Locale.US);
    private static SimpleDateFormat simpleShortDateFormat = new SimpleDateFormat("E, HH:mm", Locale.US);

    public TimeEntry(Date start) {
        this.start = start;
    }

    public TimeEntry(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public TimeEntry(Date start, Date end,long uuid) {
        super(uuid);
        this.start = start;
        this.end = end;
    }

    public TimeEntry() {
        //for serializer
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void stop() {

        if(!isStopped()) {
            end = new Date();

        } else {
            throw new RuntimeException("TimeEntry end already set");
        }
    }
    public boolean isStopped() {
        return end != null;
    }

    public long getDuration() {
        if(end!=null) {
            return end.getTime() - start.getTime();
        } else {
            return new Date().getTime() - start.getTime();
        }
    }

    public String getPrettyPrintEnd() {
        if(end != null) {
            return simpleDateFormat.format(end);
        }
        return "Open";
    }

    public String getPrettyPrintStart() {
        return simpleDateFormat.format(start);
    }

    public String getPrettyPrintDuration() {
        String duration =  DurationPrinter.printDuration(getDuration());
        if(end==null) {
            duration = ">"+duration;
        }
        return duration;
    }

    public String getPrettyPrintShortStart() {
        return simpleShortDateFormat.format(start);
    }
}
