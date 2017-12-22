package com.ti_zero.com.apptime.data.objects;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by uni on 12/22/17.
 */

public class TimeEntry {

    private Date start;
    private Date end = null;

    public TimeEntry(Date start) {
        this.start = start;
    }

    public TimeEntry(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public void stop() {

        if(!isStopped()) {
            end = new Date();

        } else {
            throw new RuntimeException("TimeEntry already set");
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
}
