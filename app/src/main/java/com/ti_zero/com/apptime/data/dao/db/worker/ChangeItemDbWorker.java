package com.ti_zero.com.apptime.data.dao.db.worker;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/24/17.
 */

public class ChangeItemDbWorker extends GenericWorkerThread {

    private AbstractItem item;
    private AppDatabase appDatabase;

    public ChangeItemDbWorker(AbstractItem item, AppDatabase appDatabase) {
        this.item = item;
        this.appDatabase = appDatabase;
    }

    @Override
    public void run() {
        if (item instanceof GroupItem) {
            GroupEntity groupEntity = convertGroupItemToGroupEntity(item.getParent().getUniqueID(),item);
            appDatabase.groupEntityDao().updateGroupEntity(groupEntity);
        } else if (item instanceof AccountItem) {
            AccountEntity accountEntity = convertAccountItemToAccountEntity(item.getParent().getUniqueID(),item);
            appDatabase.accountEntityDao().updateAccountEntity(accountEntity);
        }
        Logging.logInfo(LogTag.PERSISTENZ, "ChangeItemDbWorker finished("+item.getUniqueID()+")");
    }
}
