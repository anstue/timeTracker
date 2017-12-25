package com.ti_zero.com.apptime.data;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ti_zero.com.apptime.AppExecutors;
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

    private AppExecutors appExecutors;

    public DataAccessFacade(Context applicationContext) {
        appExecutors = new AppExecutors();
        this.appDatabase = AppDatabase.getDatabase(applicationContext, appExecutors);
        this.dataInMemoryStorage = new DataInMemoryStorage();

        initialize();
    }

    public DataInMemoryStorage getDataInMemoryStorage() {
        return dataInMemoryStorage;
    }

    private void initialize() {
        appExecutors.diskIO().execute(new InitializeMemoryDbWorker(appDatabase, dataInMemoryStorage));
    }

    @Override
    public MutableLiveData<Boolean> isInitialized() {
        return appDatabase.isInitialized();
    }

    @Override
    public void createNewItem(GroupItem parent, AbstractItem item) {
        parent.addItem(item);
        appExecutors.diskIO().execute(new CreateNewItemDbWorker(parent, item, appDatabase));
    }

    @Override
    public void changeItem(AbstractItem item) {
        appExecutors.diskIO().execute(new ChangeItemDbWorker(item, appDatabase));
    }

    @Override
    public void removeItem(long itemUUID) {
        AbstractItem item = dataInMemoryStorage.findItem(itemUUID);
        dataInMemoryStorage.removeItem(itemUUID);
        removeItemFromDB(item);
    }

    private void removeItemFromDB(AbstractItem item) {
        appExecutors.diskIO().execute(new RemoveItemDbWorker(appDatabase, item));
    }

    @Override
    public void startItem(AbstractItem item) {
        StartItemDTO addedTo = item.addTimeEntry();
        if (addedTo.isNewItem()) {
            appExecutors.diskIO().execute(new CreateNewItemDbWorker(addedTo.getItem().getParent(), addedTo.getItem(), appDatabase));
        }
        appExecutors.diskIO().execute(new NewTimeEntryDbWorker(appDatabase, addedTo.getItem()));
    }

    @Override
    public void stopItem(AbstractItem item) {
        AccountItem addedTo = item.stop();
        appExecutors.diskIO().execute(new ChangeTimeEntryDbWorker(appDatabase, addedTo));
    }

    @Override
    public void removeItem(GroupItem groupItem, int position) {
        AbstractItem itemToRemove = groupItem.getItems().get(position);
        removeItemFromDB(itemToRemove);
        groupItem.removeItem(position);

    }

}
