package com.ti_zero.com.apptime.data;

import com.ti_zero.com.apptime.data.dao.db.AppDatabase;
import com.ti_zero.com.apptime.data.objects.AbstractItem;

/**
 * Created by anstue on 12/24/17.
 */

public class DataAccessFacade implements IDataAccessFacade{

    private final DataInMemoryStorage dataInMemoryStorage;
    private final AppDatabase appDatabase;

    public DataAccessFacade(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.dataInMemoryStorage = new DataInMemoryStorage();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void createNewItem(long parentItemId, AbstractItem item) {

    }

    @Override
    public void changeItem(AbstractItem item) {

    }

    @Override
    public void removeItem(long itemId) {

    }

    @Override
    public void startItem(long itemId) {

    }

    @Override
    public void stopItem(long itemId) {

    }
}
