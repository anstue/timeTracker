package com.ti_zero.com.apptime.data;

import android.accounts.Account;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ti_zero.com.apptime.AppExecutors;
import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;
import com.ti_zero.com.apptime.data.dao.db.worker.ChangeItemDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.ChangeTimeEntryDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.CreateNewItemDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.DbToJsonWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.InitializeMemoryDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.NewTimeEntryDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.RemoveItemDbWorker;
import com.ti_zero.com.apptime.data.dao.db.worker.RemoveTimeEntryDbWorker;
import com.ti_zero.com.apptime.data.dto.StartItemDTO;
import com.ti_zero.com.apptime.data.dto.TimeEntryDTO;
import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.ObservableWithUUID;
import com.ti_zero.com.apptime.data.objects.TimeEntry;
import com.ti_zero.com.apptime.helper.LogTag;
import com.ti_zero.com.apptime.helper.Logging;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */

public class DataAccessFacade implements IDataAccessFacade {

    //TODO refactor give only entities to the dbWorkers
    private final DataInMemoryStorage dataInMemoryStorage;
    private final AppDatabase appDatabase;

    private AppExecutors appExecutors;
    private Context context;

    public DataAccessFacade(Context applicationContext) {
        appExecutors = new AppExecutors();
        this.appDatabase = AppDatabase.getDatabase(applicationContext, appExecutors);
        this.dataInMemoryStorage = new DataInMemoryStorage();
        this.context = applicationContext;

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
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "createNewItem item:" + item.getName());
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
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "removeItemUUID item:" + item.getName());
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
        if (addedTo.getTimeEntry() != null) {
            appExecutors.diskIO().execute(new ChangeTimeEntryDbWorker(appDatabase, addedTo.getItem(), addedTo.getTimeEntry()));

        }
        item.notifyPropertyChanged(BR.btnToggleText);
        item.notifyPropertyChanged(BR.running);
        //TODO save every item in hierarchy eg. for lastUsage(not just the first and last one)
        appExecutors.diskIO().execute(new ChangeItemDbWorker(item, appDatabase));
        appExecutors.diskIO().execute(new ChangeItemDbWorker(addedTo.getItem(), appDatabase));
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "startItem item:" + item.getName());
    }

    @Override
    public void stopItem(AbstractItem item) {
        AccountItem addedTo = item.stop();
        addedTo.notifyPropertyChanged(BR.btnToggleText);
        addedTo.notifyPropertyChanged(BR.running);
        item.notifyPropertyChanged(BR.btnToggleText);
        item.notifyPropertyChanged(BR.running);
        appExecutors.diskIO().execute(new ChangeTimeEntryDbWorker(appDatabase, addedTo));
        //TODO save every item in hierarchy eg. for lastUsage(not just the first and last one)
        if (item.getUniqueID() != ObservableWithUUID.ROOT_UUID) {
            appExecutors.diskIO().execute(new ChangeItemDbWorker(item, appDatabase));
        }
        appExecutors.diskIO().execute(new ChangeItemDbWorker(addedTo, appDatabase));
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "stopItem item:" + item.getName());
    }

    @Override
    public void removeItem(GroupItem groupItem, int position) {
        AbstractItem itemToRemove = groupItem.getItems().get(position);
        removeItemFromDB(itemToRemove);
        groupItem.removeItem(position);
        itemToRemove.setParent(null);
        itemToRemove.notifyPropertyChanged(BR.parent);
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "removeItem item:" + itemToRemove.getName());
    }

    @Override
    public void generateJson(String fileName) {
        appExecutors.diskIO().execute(new DbToJsonWorker(appDatabase, fileName, context));
    }

    @Override
    public void loadFromJson(File file) {

    }

    @Override
    public AbstractItem findItem(long uuid) {
        return dataInMemoryStorage.findItem(uuid);
    }

    @Override
    public void removeTimeEntry(AccountItem accountItem, TimeEntry timeEntry) {
        accountItem.removeTimeEntry(timeEntry);
        appExecutors.diskIO().execute(new RemoveTimeEntryDbWorker(appDatabase, timeEntry));
        Logging.logInfo(LogTag.DATA_ACCESS_FACADE, "Removed timeEntry");
    }

    @Override
    public void addTimeEntry(AccountItem item, TimeEntry timeEntry) {
        item.getTimeEntries().add(timeEntry);
        appExecutors.diskIO().execute(new NewTimeEntryDbWorker(appDatabase, item, timeEntry));
    }

    @Override
    public void startAndChangeItemRunningTimeEntry(AbstractItem item, int minutes) {

        if (item.isRunning()) {
            changeCurrentTimeEntry(item, minutes);
        } else {
            startItem(item);
            changeCurrentTimeEntry(item, minutes);
        }
        item.notifyPropertyChanged(BR.btnToggleText);
        item.notifyPropertyChanged(BR.running);
        //TODO unit tests
    }

    private void changeCurrentTimeEntry(AbstractItem item, int i) {
        TimeEntryDTO dto = item.findCurrentTimeEntry();
        Date nextToLast = getNextToLastTimeEntry(dto.getItem().getTimeEntries());
        Calendar start = Calendar.getInstance();
        start.setTime(dto.getTimEntry().getStart());
        start.add(Calendar.MINUTE, i);
        Date currentDate = new Date();
        //cannot start in future
        if(start.getTime().getTime()>currentDate.getTime()) {
            start.setTime(currentDate);
        }
        //it is not possible that timeEntry starts before nextToLast timeEntry
        if(start.getTime().getTime()<nextToLast.getTime()) {
            start.setTime(nextToLast);
            start.add(Calendar.SECOND,1);
        }
        dto.getTimEntry().setStart(start.getTime());
        appExecutors.diskIO().execute(new ChangeTimeEntryDbWorker(appDatabase, dto.getItem(), dto.getTimEntry()));

    }

    private Date getNextToLastTimeEntry(List<TimeEntry> timeEntries) {
        if(timeEntries.size()>=2) {
            return timeEntries.get(timeEntries.size()-2).getEnd();
        }
        return null;
    }

}
