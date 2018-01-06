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

public class RemoveTimeEntryDbWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;
    private TimeEntry timeEntry;


    public RemoveTimeEntryDbWorker(AppDatabase appDatabase, TimeEntry timeEntry) {
        this.appDatabase = appDatabase;
        this.timeEntry = timeEntry;
    }

    @Override
    public void run() {
        appDatabase.timeEntityDao().deleteTimeEntity(timeEntry.getUniqueID());
        Logging.logInfo(LogTag.PERSISTENZ, "RemoveTimeEntryDbWorker finished");
    }
}
