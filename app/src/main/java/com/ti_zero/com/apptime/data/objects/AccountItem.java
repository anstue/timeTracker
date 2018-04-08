package com.ti_zero.com.apptime.data.objects;

import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.R;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.data.dto.TimeEntryDTO;
import com.ti_zero.com.apptime.helper.DurationPrinter;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;
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
    private TimeEntry runningEntry;
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

    public void addTimeEntry(TimeEntry timeEntry) {
        timeEntries.add(0, timeEntry);
        if (runningEntry != null && !runningEntry.isStopped() && !timeEntry.isStopped()) {
            throw new RuntimeException("Multiple timeEntries running within one accountItem");
        } else if (!timeEntry.isStopped()) {
            runningEntry = timeEntry;
        }
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    @Override
    public boolean isRunning() {
        return getRunningTimeEntry() != null && !getRunningTimeEntry().isStopped();
    }

    public AccountItem stop() {
        if (getRunningTimeEntry() != null) {
            getRunningTimeEntry().stop();
            setLastUsage(new Date());
            notifyPropertyChanged(BR.btnToggleText);
            notifyPropertyChanged(BR.running);
        }
        return this;
    }

    public TimeEntry getRunningTimeEntry() {
        return runningEntry;
    }

    public StartItemDTO addTimeEntry() {
        //make sure only one time_entry is running
        TimeEntry timeEntry = null;
        if (getRunningTimeEntry() != null && !getRunningTimeEntry().isStopped()) {
            timeEntry = getRunningTimeEntry();
            getRunningTimeEntry().stop();
        }
        TimeEntry newTimeEntry = new TimeEntry(new Date());
        addTimeEntry(newTimeEntry);

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
        return (getRunningTimeEntry() != null) ? getRunningTimeEntry().getPrettyPrintShortStart() : "";
    }

    @Override
    public TimeEntryDTO findCurrentTimeEntry() {
        TimeEntry timeEntry = getRunningTimeEntry();
        if (timeEntry != null && timeEntry.getEnd() == null) {
            return new TimeEntryDTO(timeEntry, this);
        }
        return null;
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
