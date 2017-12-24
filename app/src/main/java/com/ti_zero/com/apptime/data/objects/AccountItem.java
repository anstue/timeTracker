package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.helper.DurationPrinter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class AccountItem extends AbstractItem {

    private volatile List<TimeEntry> timeEntries;
    private AbstractItem parent;

    public AccountItem(String name, String description, Date lastUsage, boolean favorite) {
        super(name, description, lastUsage, favorite);
        timeEntries = new ArrayList<>();
    }

    public AccountItem(String name, String description, Date lastUsage, boolean favorite, List<TimeEntry> timeEntries) {
        super(name, description, lastUsage, favorite);
        this.timeEntries = timeEntries;
    }

    public AccountItem() {
        //for serializer
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    @Override
    public boolean isRunning() {
            return timeEntries.size()>0 && !getLastTimeEntry().isStopped();
    }
    public void stop() {
        if(timeEntries.size()>0) {
            getLastTimeEntry().stop();
        }
    }

    private TimeEntry getLastTimeEntry() {
        return timeEntries.get(timeEntries.size()-1);
    }

    public void addTimeEntry(){
        //make sure only one timeEntry is running
        if(timeEntries.size()>0 && !getLastTimeEntry().isStopped()) {
            getLastTimeEntry().stop();
        }
        timeEntries.add(new TimeEntry(new Date()));
    }

    @Override
    public List<AbstractItem> getChildren() {
        return null;
    }

    @Override
    public AbstractItem getParent() {
        return parent;
    }

    @Override
    public void setParent(AbstractItem item) {
        this.parent=item;
    }

    @Override
    public long getTotalTime() {
        long sum=0;
        for(TimeEntry t: timeEntries) {
           sum+=t.getDuration();
        }
        return sum;
    }
}
