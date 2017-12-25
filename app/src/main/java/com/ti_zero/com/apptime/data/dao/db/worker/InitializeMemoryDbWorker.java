package com.ti_zero.com.apptime.data.dao.db.worker;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.objects.ObjWithUUID;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

/**
 * Created by anstue on 12/24/17.
 */

public class InitializeMemoryDbWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;

    public InitializeMemoryDbWorker(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public void run() {
        //TODO just for testing
        appDatabase.groupEntityDao().deleteGroupEntity(ObjWithUUID.ROOT_UUID);
        //create root item
        GroupEntity groupEntity = convertGroupItemToGroupEntity(null, new ObjectFactory().getRootItem());
        appDatabase.groupEntityDao().addGroupEntity(groupEntity);
        appDatabase.setInitialized();
        Logging.logInfo(LogTag.PERSISTENZ, "InitializeMemoryDbWorker finished");
    }
}
