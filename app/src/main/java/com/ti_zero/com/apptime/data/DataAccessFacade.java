package com.ti_zero.com.apptime.data;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;
import com.ti_zero.com.apptime.data.dao.db.worker.ChangeItemDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.ChangeTimeEntryDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.CreateNewItemDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.InitializeMemoryDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.NewTimeEntryDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.RemoveItemDbWorker;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;

import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */

public class DataAccessFacade implements IDataAccessFacade {

    //TODO refactor give only entities to the dbWorkers
    private final DataInMemoryStorage dataInMemoryStorage;
    private final AppDatabase appDatabase;

    private Handler dbWorkerHandler;

    public DataAccessFacade(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.dataInMemoryStorage = new DataInMemoryStorage();
        HandlerThread thread = new HandlerThread("DbWorkerThread");
        thread.start();
        dbWorkerHandler = new Handler(thread.getLooper());
    }

    public DataInMemoryStorage getDataInMemoryStorage() {
        return dataInMemoryStorage;
    }

    @Override
    public void initialize() {
        dbWorkerHandler.post(new InitializeMemoryDbWorker(appDatabase));
    }

    @Override
    public boolean isInitialized() {
        return appDatabase.isInitialized();
    }

    @Override
    public void createNewItem(GroupItem parent, AbstractItem item) {
        parent.addItem(item);
        dbWorkerHandler.post(new CreateNewItemDbWorker(parent, item, appDatabase));
    }

    @Override
    public void changeItem(AbstractItem item) {
        dbWorkerHandler.post(new ChangeItemDbWorker(item, appDatabase));
    }

    @Override
    public void removeItem(long itemUUID) {
        AbstractItem item = dataInMemoryStorage.findItem(itemUUID);
        dataInMemoryStorage.removeItem(itemUUID);
        removeItemFromDB(item);
    }

    private void removeItemFromDB(AbstractItem item) {
        dbWorkerHandler.post(new RemoveItemDbWorker(appDatabase, item));
    }

    @Override
    public void startItem(AbstractItem item) {
        StartItemDTO addedTo = item.addTimeEntry();
        if (addedTo.isNewItem()) {
            dbWorkerHandler.post(new CreateNewItemDbWorker(addedTo.getItem().getParent(), addedTo.getItem(), appDatabase));
        }
        dbWorkerHandler.post(new NewTimeEntryDbWorker(appDatabase, addedTo.getItem()));
    }

    @Override
    public void stopItem(AbstractItem item) {
        AccountItem addedTo = item.stop();
        dbWorkerHandler.post(new ChangeTimeEntryDbWorker(appDatabase, addedTo));
    }

    @Override
    public void removeItem(GroupItem groupItem, int position) {
        AbstractItem itemToRemove = groupItem.getItems().get(position);
        removeItemFromDB(itemToRemove);
        groupItem.removeItem(position);

    }

}
