package com.ti_zero.com.apptime.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.ti_zero.com.apptime.AppExecutors;
import com.ti_zero.com.apptime.BR;
import com.ti_zero.com.apptime.BaseApp;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
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

    private static DataAccessFacade instance;

    public DataAccessFacade(BaseApp baseApp) {
        this.appExecutors = baseApp.getAppExecutors();
        this.appDatabase = baseApp.getDatabase();
        this.dataInMemoryStorage = new DataInMemoryStorage();
        this.context = baseApp;

        initialize();
    }

    public DataInMemoryStorage getDataInMemoryStorage() {
        return dataInMemoryStorage;
    }

    private void initialize() {
        appExecutors.diskIO().execute(new InitializeMemoryDbWorker(appDatabase, dataInMemoryStorage));
    }

    public static DataAccessFacade getInstance(BaseApp baseApp) {
        if (instance == null) {
            synchronized (DataAccessFacade.class) {
                instance = new DataAccessFacade(baseApp);
            }
        }
        return instance;
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
    public void stopOtherItemsAndStartItem(AbstractItem item) {

        stopAllItems(item);

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
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "stopOtherItemsAndStartItem item:" + item.getName());
    }

    private void stopAllItems(AbstractItem item) {
        if (getDataInMemoryStorage().getRootItem().isRunning()) {
            //check rootItem maybe sth is running in another group
            //check if item itself is running(child of group)
            if (!item.isRunning()) {
                stopItem(getDataInMemoryStorage().getRootItem());
                Logging.logDebug(LogTag.UI, "Stopped item over root item: " + item.getName());
            }
        }
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
    public void removeItem(AbstractItem itemToRemove) {
        removeItemFromDB(itemToRemove);
        itemToRemove.getParent().removeItem(itemToRemove);
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
        item.addTimeEntry(timeEntry);
        appExecutors.diskIO().execute(new NewTimeEntryDbWorker(appDatabase, item, timeEntry));
        Logging.logInfo(LogTag.DATA_ACCESS_FACADE, "timeEntry added");
    }

    @Override
    public void startAndChangeItemRunningTimeEntry(AbstractItem item, int minutes) {

        if (item.isRunning()) {
            changeCurrentTimeEntry(item, minutes);
        } else {
            stopOtherItemsAndStartItem(item);
            changeCurrentTimeEntry(item, minutes);
        }
        item.notifyPropertyChanged(BR.btnToggleText);
        item.notifyPropertyChanged(BR.running);
    }

    @Override
    public void undoRemoveItem(GroupItem parent, AbstractItem removedItem, int position) {
        parent.addItem(removedItem, position);
        appExecutors.diskIO().execute(new CreateNewItemDbWorker(parent, removedItem, appDatabase));
        Logging.logDebug(LogTag.DATA_ACCESS_FACADE, "undoRemoveItem item:" + removedItem.getName());
    }

    @Override
    public void undoRemoveTimeEntry(AccountItem parent, TimeEntry timeEntryToBeRemoved) {
        addTimeEntry(parent, timeEntryToBeRemoved);
        Logging.logInfo(LogTag.DATA_ACCESS_FACADE, "timeEntry undo remove");
    }

    private void changeCurrentTimeEntry(AbstractItem item, int i) {
        TimeEntryDTO dto = item.findCurrentTimeEntry();
        Date nextToLast = getNextToRunningTimeEntry(dto.getItem().getTimeEntries(), dto);
        Calendar start = Calendar.getInstance();
        start.setTime(dto.getTimEntry().getStart());
        start.add(Calendar.MINUTE, i);
        Date currentDate = new Date();
        //cannot start in future
        if (start.getTime().getTime() > currentDate.getTime()) {
            start.setTime(currentDate);
        }
        //it is not possible that timeEntry starts before nextToLast timeEntry
        if (nextToLast != null && start.getTime().getTime() < nextToLast.getTime()) {
            start.setTime(nextToLast);
            start.add(Calendar.SECOND, 1);
        }
        dto.getTimEntry().setStart(start.getTime());
        appExecutors.diskIO().execute(new ChangeTimeEntryDbWorker(appDatabase, dto.getItem(), dto.getTimEntry()));

    }

    private Date getNextToRunningTimeEntry(List<TimeEntry> timeEntries, TimeEntryDTO currentTimeEntry) {
        if (timeEntries.size() >= 2) {
            int currentIdx=timeEntries.indexOf(currentTimeEntry.getTimEntry());
            if(currentIdx+1<timeEntries.size()) {
                return timeEntries.get(currentIdx+1).getEnd();
            }
        }
        return null;
    }

}
