package com.ti_zero.com.apptime.data.dao.db.worker;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/24/17.
 */

public class RemoveItemDbWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;
    private AbstractItem item;

    public RemoveItemDbWorker(AppDatabase appDatabase, AbstractItem item) {
        this.appDatabase = appDatabase;
        this.item = item;
    }

    @Override
    public void run() {
        if (item instanceof GroupItem) {
            appDatabase.groupEntityDao().deleteGroupEntity(item.getUniqueID());
        } else if (item instanceof AccountItem) {
            appDatabase.accountEntityDao().deleteAccountEntity(item.getUniqueID());
        }
        Logging.logInfo(LogTag.PERSISTENZ, "RemoveItemDbWorker finished("+item.getUniqueID()+")");
    }
}
