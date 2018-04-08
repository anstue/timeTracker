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

public class NewTimeEntryDbWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;
    private AccountItem addedTo;
    private TimeEntry timeEntry = null;

    public NewTimeEntryDbWorker(AppDatabase appDatabase, AccountItem addedTo) {
        this.appDatabase = appDatabase;
        this.addedTo = addedTo;
    }

    public NewTimeEntryDbWorker(AppDatabase appDatabase, AccountItem addedTo, TimeEntry timeEntry) {
        this.appDatabase = appDatabase;
        this.addedTo = addedTo;
        this.timeEntry = timeEntry;
    }

    @Override
    public void run() {
        TimeEntry timeEntryToAdd = timeEntry;
        if (timeEntry == null) {
            timeEntryToAdd = addedTo.getRunningTimeEntry();
        }
        TimeEntity timeEntity = new TimeEntity(timeEntryToAdd.getUniqueID(), addedTo.getUniqueID(),
                timeEntryToAdd.getStart().getTime(), timeEntryToAdd.getEnd() == null ? -1 : timeEntryToAdd.getEnd().getTime());
        appDatabase.timeEntityDao().addTimeEntity(timeEntity);
        Logging.logInfo(LogTag.PERSISTENZ, "NewTimeEntryDbWorker finished");
    }
}
