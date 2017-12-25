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

    public NewTimeEntryDbWorker(AppDatabase appDatabase, AccountItem addedTo) {
        this.appDatabase = appDatabase;
        this.addedTo = addedTo;
    }

    @Override
    public void run() {
        TimeEntry lastTimeEntry = addedTo.getLastTimeEntry();
        TimeEntity timeEntity = new TimeEntity(lastTimeEntry.getUniqueID(),addedTo.getUniqueID(),
                lastTimeEntry.getStart().getTime(),-1);
        appDatabase.timeEntityDao().addTimeEntity(timeEntity);
        Logging.logInfo(LogTag.PERSISTENZ, "NewTimeEntryDbWorker finished");
    }
}
