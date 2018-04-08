package com.ti_zero.com.apptime.data.dao.db.worker;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/25/17.
 */

public class ChangeTimeEntryDbWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;
    private AccountItem item;
    private TimeEntry timeEntry = null;

    public ChangeTimeEntryDbWorker(AppDatabase appDatabase, AccountItem item) {
        this.appDatabase = appDatabase;
        this.item = item;
    }

    public ChangeTimeEntryDbWorker(AppDatabase appDatabase, AccountItem item, TimeEntry timeEntry) {
        this.appDatabase = appDatabase;
        this.item = item;
        this.timeEntry = timeEntry;
    }

    @Override
    public void run() {
        TimeEntry timeEntryToChange = timeEntry;
        if (timeEntryToChange == null) {
            timeEntryToChange = item.getRunningTimeEntry();
        }
        TimeEntity timeEntity = new TimeEntity(timeEntryToChange.getUniqueID(), item.getUniqueID(),
                timeEntryToChange.getStart().getTime(), timeEntryToChange.getEnd() == null ? -1 : timeEntryToChange.getEnd().getTime());
        appDatabase.timeEntityDao().updateTimeEntity(timeEntity);
        Logging.logInfo(LogTag.PERSISTENZ, "ChangeTimeEntryDbWorker finished");
    }
}
