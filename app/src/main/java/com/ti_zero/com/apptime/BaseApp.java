package com.ti_zero.com.apptime;

import android.app.Application;

import com.ti_zero.com.apptime.data.DataAccessFacade;
import com.ti_zero.com.apptime.data.dao.db.AppDatabase;

/**
 * Created by anstue on 4/7/18.
 */

public class BaseApp extends Application {

    private DataAccessFacade dataAccessFacade;
    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        appExecutors = new AppExecutors();
    }


    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, appExecutors);
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    public DataAccessFacade getDataAccessFacade() {
        return DataAccessFacade.getInstance(this);
    }



}
