package com.ti_zero.com.apptime.data.objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by uni on 12/22/17.
 */

public class TimeEntry implements Serializable {

    private Date start;
    private Date end = null;

    public TimeEntry(Date start) {
        this.start = start;
    }

    public TimeEntry(Date start, Date end) {
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
}
