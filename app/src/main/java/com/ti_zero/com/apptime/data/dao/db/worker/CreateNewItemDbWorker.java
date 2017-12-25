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

public class CreateNewItemDbWorker extends GenericWorkerThread  {

    private GroupItem parent;
    private AbstractItem item;
    private AppDatabase appDatabase;

    public CreateNewItemDbWorker(GroupItem parent, AbstractItem item, AppDatabase appDatabase) {
        this.parent = parent;
        this.item = item;
        this.appDatabase=appDatabase;
    }

    @Override
    public void run() {
        if (item instanceof GroupItem) {
            GroupEntity groupEntity = convertGroupItemToGroupEntity(parent.getUniqueID(), item);
            appDatabase.groupEntityDao().addGroupEntity(groupEntity);
        } else if (item instanceof AccountItem) {
            AccountEntity accountEntity = convertAccountItemToAccountEntity(parent.getUniqueID(), item);
            appDatabase.accountEntityDao().addAccountEntity(accountEntity);
        }
        Logging.logInfo(LogTag.PERSISTENZ, "CreateNewItemDbWorker finished("+item.getUniqueID()+")");
    }
}
