package com.ti_zero.com.apptime.data.dao.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ti_zero.com.apptime.data.dao.db.entities.AccountEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;
import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;

/**
 * Created by anstue on 12/24/17.
 */


@Database(entities = {AccountEntity.class,  TimeEntity.class, GroupEntity.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract AccountEntityDao accountEntityDao();
    public abstract GroupEntityDao groupEntityDao();
    public abstract TimeEntityDao timeEntityDao();

    private volatile boolean initialized=false;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "itemdatabase")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static AppDatabase getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(AppDatabase INSTANCE) {
        AppDatabase.INSTANCE = INSTANCE;
    }

    public boolean isInitialized() {
        return initialized;
    }

    /**
     * you can only set it to true
     */
    public void setInitialized() {
        this.initialized = true;
    }
}
