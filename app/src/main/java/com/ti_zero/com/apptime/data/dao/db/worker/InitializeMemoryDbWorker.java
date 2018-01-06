package com.ti_zero.com.apptime.data.dao.db.worker;

import com.ti_zero.com.apptime.data.DataInMemoryStorage;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.ObservableWithUUID;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.data.objects.factories.ObjectFactory;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

import java.util.Date;
import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */

public class InitializeMemoryDbWorker extends GenericWorkerThread {

    private AppDatabase appDatabase;
    private DataInMemoryStorage dataInMemoryStorage;

    public InitializeMemoryDbWorker(AppDatabase appDatabase, DataInMemoryStorage dataInMemoryStorage) {
        this.appDatabase = appDatabase;
        this.dataInMemoryStorage = dataInMemoryStorage;
    }

    @Override
    public void run() {
        //appDatabase.groupEntityDao().deleteGroupEntity(ObservableWithUUID.ROOT_UUID);

        List<GroupEntity> groupEntities = appDatabase.groupEntityDao().getGroupEntity(ObservableWithUUID.ROOT_UUID);
        if (groupEntities.size() == 0) {
            //create root item
            GroupEntity groupEntity = convertGroupItemToGroupEntity(null, new ObjectFactory().getRootItem());
            appDatabase.groupEntityDao().addGroupEntity(groupEntity);
        } else if (groupEntities.size() == 1) {
            //load db
            GroupEntity groupEntity = groupEntities.get(0);
            GroupItem rootItem = convertGroupEntityToGroupItem(groupEntity, null);
            loadLayer(rootItem);
            dataInMemoryStorage.setRootItem(rootItem);
            Logging.logInfo(LogTag.PERSISTENZ, "Loading existing DB in memory succeeded");

        } else {
            throw new RuntimeException("More than one root element found: " + groupEntities.size());
        }
        appDatabase.setInitialized();
        Logging.logInfo(LogTag.PERSISTENZ, "InitializeMemoryDbWorker finished");
    }

    private void loadLayer(GroupItem groupItem) {
        loadGroupEntities(groupItem);
        loadAccountEntities(groupItem);
    }

    private void loadGroupEntities(GroupItem groupItem) {
        List<GroupEntity> groupEntities = appDatabase.groupEntityDao().getGroupEntities(groupItem.getUniqueID());
        for (GroupEntity entity : groupEntities) {
            GroupItem item = convertGroupEntityToGroupItem(entity, groupItem);
            loadLayer(item);
        }
    }

    private void loadAccountEntities(GroupItem groupItem) {
        List<AccountEntity> accountEntities = appDatabase.accountEntityDao().getAccountEntities(groupItem.getUniqueID());
        for (AccountEntity entity : accountEntities) {
            AccountItem accountItem = convertAccountEntityToAccountItem(entity, groupItem);
            loadTimeEntries(entity, accountItem);
        }
    }

    private void loadTimeEntries(AccountEntity accountEntity, AccountItem accountItem) {
        List<TimeEntity> timeEntities = appDatabase.timeEntityDao().getTimeEntities(accountEntity.getAccountEntityId());
        int i=0;
        for (TimeEntity entity : timeEntities) {
            insertTimeEntryIntoAccountItem(entity, accountItem, i==timeEntities.size()-1);
            i++;
        }


    }

    private void insertTimeEntryIntoAccountItem(TimeEntity entity, AccountItem parent, boolean lastEntry) {
        Date end = null;
        if (entity.getEnd() != -1) {
            end = getJavaDate(entity.getEnd());
        } else if(!lastEntry) {
            end = new Date();
            entity.setEnd(end.getTime());
            appDatabase.timeEntityDao().updateTimeEntity(entity);
            Logging.logError(LogTag.PERSISTENZ, "TimeEntry for item was not closed correctly, closed it with NOW");
        }
        TimeEntry newTimeEntry = new TimeEntry(getJavaDate(entity.getStart()), end, entity.getTimeEntityId());
        parent.getTimeEntries().add(newTimeEntry);
    }
}
