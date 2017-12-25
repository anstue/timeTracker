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

    public ChangeTimeEntryDbWorker(AppDatabase appDatabase, AccountItem item) {
        this.appDatabase = appDatabase;
        this.item = item;
    }

    @Override
    public void run() {
        TimeEntry lastTimeEntry = item.getLastTimeEntry();
        TimeEntity timeEntity = new TimeEntity(lastTimeEntry.getUniqueID(),item.getUniqueID(),
                lastTimeEntry.getStart().getTime(), lastTimeEntry.getEnd().getTime());
        appDatabase.timeEntityDao().updateTimeEntity(timeEntity);
        Logging.logInfo(LogTag.PERSISTENZ, "ChangeTimeEntryDbWorker finished");
    }
}
