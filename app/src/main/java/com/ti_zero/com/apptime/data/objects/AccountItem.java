package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.helper.DurationPrinter;
import com.ti_zero.com.apptime.helper.TimeHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by uni on 12/22/17.
 */

public class AccountItem extends AbstractItem {

    private volatile List<TimeEntry> timeEntries;
    private GroupItem parent;

    public AccountItem(String name, String description, Date lastUsage, boolean favorite) {
        super(name, description, lastUsage, favorite);
        timeEntries = new ArrayList<>();
    }

    public AccountItem(String name, String description, Date lastUsage, boolean favorite, long uuid) {
        super(name, description, lastUsage, favorite, uuid);
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
        return timeEntries.size() > 0 && !getLastTimeEntry().isStopped();
    }

    public AccountItem stop() {
        if (timeEntries.size() > 0) {
            getLastTimeEntry().stop();
            setLastUsage(new Date());
            notifyPropertyChanged(BR.btnToggleText);
            notifyPropertyChanged(BR.running);
        }
        return this;
    }

    public TimeEntry getLastTimeEntry() {
        return timeEntries.get(timeEntries.size() - 1);
    }

    public StartItemDTO addTimeEntry() {
        //make sure only one time_entry is running
        TimeEntry timeEntry = null;
        if (timeEntries.size() > 0 && !getLastTimeEntry().isStopped()) {
            timeEntry = getLastTimeEntry();
            getLastTimeEntry().stop();
        }
        timeEntries.add(new TimeEntry(new Date()));
        setLastUsage(new Date());
        notifyPropertyChanged(BR.btnToggleText);
        notifyPropertyChanged(BR.running);
        return new StartItemDTO(this, false, timeEntry);
    }

    @Override
    public List<AbstractItem> getChildren() {
        return null;
    }

    @Override
    public GroupItem getParent() {
        return parent;
    }

    @Override
    public void setParent(GroupItem item) {
        this.parent = item;
    }

    @Override
    public long getTodayTime() {
        long todayTime = 0;
        long todayBegin = TimeHelper.getBeginningOfTheDay().getTime();
        long todayEnd = TimeHelper.getEndOfTheDay().getTime();
        for (TimeEntry timeEntry : timeEntries) {
            todayTime += TimeHelper.getTimeWithinBoundries(todayBegin, todayEnd, timeEntry);
        }
        return todayTime;
    }

    @Override
    public String getShortStartTime() {
        return getLastTimeEntry().getPrettyPrintShortStart();
    }

    @Override
    public long getTotalTime() {
        long sum = 0;
        for (TimeEntry t : timeEntries) {
            sum += t.getDuration();
        }
        return sum;
    }

    public void removeTimeEntry(TimeEntry timeEntry) {
        timeEntries.remove(timeEntry);
        this.notifyPropertyChanged(BR.todayTimePrettyPrint);
        this.notifyPropertyChanged(BR.totalTimePrettyPrint);
    }
}
